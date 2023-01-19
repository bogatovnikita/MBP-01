package yin_kio.duplicates.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.gateways.Ads
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.models.UniteWay
import yin_kio.duplicates.domain.use_cases.DuplicateRemover
import yin_kio.duplicates.domain.use_cases.DuplicatesUseCasesImpl
import yin_kio.duplicates.domain.use_cases.UniteUseCase


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    private lateinit var state: MutableStateHolder
    private val dispatcher = StandardTestDispatcher()
    private val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var files: Files
    private lateinit var permissions: Permissions
    private lateinit var useCase: DuplicatesUseCasesImpl
    private lateinit var duplicateRemover: DuplicateRemover
    private val uniteUseCase: UniteUseCase = mockk()
    private val ads: Ads = mockk()

    @BeforeEach
    fun setup() = runTest(dispatcher){
        files = mockk()
        permissions = mockk()
        duplicateRemover = mockk()
        state = MutableStateHolder(coroutineScope, dispatcher)

        coEvery { ads.preloadAd() } returns Unit
        coEvery { uniteUseCase.unite() } returns Unit
        every { permissions.hasStoragePermissions } returns true
        useCase = createDuplicatesUseCase()
        waitCoroutines()
    }

    @Test
    fun `init with has permission`() = runTest(dispatcher){
        val state = MutableStateHolder(this, coroutineContext)
        createDuplicatesUseCase(stateHolder = state)
        state.apply {
            assertTrue(duplicatesLists.isEmpty())
            assertTrue(isInProgress)
            assertEquals(Destination.List, state.destination)
            waitCoroutines()
            assertFalse(isInProgress)
            assertTrue(duplicatesLists.isNotEmpty())
            assertEquals(Destination.List, state.destination)
        }
    }

    @Test
    fun `init with has not permission`() = runTest(dispatcher) {
        every { permissions.hasStoragePermissions } returns false

        createDuplicatesUseCase()
        waitCoroutines()

        assertEquals(Destination.Permission, state.destination)
    }

    @Test
    fun `updateFiles with permission`() = runTest(dispatcher){
        every { permissions.hasStoragePermissions } returns false

        val useCase = createDuplicatesUseCase()
        waitCoroutines()

        assertEquals(Destination.Permission, state.destination)

        every { permissions.hasStoragePermissions } returns true

        useCase.updateFiles()
        waitCoroutines()

        assertEquals(Destination.List, state.destination)
    }

    @Test
    fun `updateFiles without permission`() = runTest(dispatcher){
        state.apply {
            val oldDuplicates = duplicatesLists

            val useCase = createDuplicatesUseCase()
            waitCoroutines()

            useCase.updateFiles()
            delay(1)
            assertTrue(isInProgress)
            waitCoroutines()
            assertFalse(isInProgress)
            assertFalse(duplicatesLists === oldDuplicates)
            assertEquals(Destination.List, state.destination)
        }
    }

    @Test
    fun switchGroupSelection() = runTest{
        useCase.switchGroupSelection(0)

        state.apply {
            assertTrue(selected.isNotEmpty())
            assertTrue(selected[0]!!.containsAll(duplicatesLists[0].data))

            useCase.switchGroupSelection(0)

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun switchItemSelection() = runTest {
        useCase.switchItemSelection(0, FIRST_FILE)
        useCase.switchItemSelection(0, SECOND_FILE)

        state.apply {
            assertTrue(selected[0]!!.containsAll(duplicatesLists[0].data))

            useCase.switchItemSelection(0, FIRST_FILE)

            assertFalse(isItemSelected(0, FIRST_FILE))
            assertTrue(isItemSelected(0, SECOND_FILE))

            useCase.switchItemSelection(0, SECOND_FILE)

            assertNull(selected[0])
            assertTrue(selected.isEmpty())
        }
    }



    @Test
    fun navigate() = runTest{
        Destination.values().forEach {
            useCase.navigate(it)
            assertEquals(it, state.destination)
        }
    }



    @Test
    fun `closeInter with item selection`() = runTest{
        state.uniteWay = UniteWay.Selected
        useCase.closeInter()
        assertEquals(Destination.AskContinue, state.destination)

        state.uniteWay = UniteWay.All
        useCase.closeInter()
        assertEquals(Destination.AdvicesWithDialog, state.destination)
    }



    @Test
    fun continueUniting(){
        useCase.continueUniting()
        assertEquals(Destination.List, state.destination)
    }

    @Test
    fun completeUniting() {
        useCase.completeUniting()
        assertEquals(Destination.Advices, state.destination)
    }

    @Test
    fun unite() = runTest{
        useCase.unite()
        coVerify { uniteUseCase.unite() }
    }


    private suspend fun createDuplicatesUseCase(stateHolder: MutableStateHolder = state): DuplicatesUseCasesImpl {
        coEvery { files.getImages() } returns listOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE)).also { delay(50) }

        return DuplicatesUseCasesImpl(
            state = stateHolder,
            files = files,
            imagesComparator = imagesComparator(),
            permissions = permissions,
            coroutineScope = coroutineScope,
            coroutineContext = dispatcher,
            uniteUseCase = uniteUseCase
        )
    }

    private fun imagesComparator() = object : ImagesComparator {
        override fun invoke(p1: ImageInfo, p2: ImageInfo): Boolean {
            return true
        }
    }

    private fun TestScope.waitCoroutines(){
        advanceUntilIdle()
    }

    companion object{
        private const val FIRST_FILE = "a"
        private const val SECOND_FILE = "b"
    }

}