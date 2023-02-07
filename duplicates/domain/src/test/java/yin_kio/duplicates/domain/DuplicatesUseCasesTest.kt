package yin_kio.duplicates.domain

import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.models.UniteWay
import yin_kio.duplicates.domain.use_cases.DuplicatesUseCasesImpl
import yin_kio.duplicates.domain.use_cases.UniteUseCase


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCasesTest {

    private lateinit var state: MutableStateHolder
    private val dispatcher = StandardTestDispatcher()
    private val coroutineScope = CoroutineScope(dispatcher)
    private val files: Files = mockk()
    private val permissions: Permissions = mockk()
    private lateinit var useCases: DuplicatesUseCasesImpl
    private val uniteUseCase: UniteUseCase = spyk()

    @BeforeEach
    fun setup() = runTest(dispatcher){
        state = MutableStateHolder(coroutineScope, dispatcher)

        every { permissions.hasStoragePermissions } returns true

        useCases = createDuplicatesUseCase()
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
        val useCase = createDuplicatesUseCase()
        assertHasNotPermissions()
        assertHasFiles(useCase)
        assertHasNotFiles(useCase)
        assertProgressChanging(useCase)
    }

    private fun TestScope.assertHasNotFiles(useCase: DuplicatesUseCasesImpl) {
        coEvery { files.getImages() } returns emptyList()
        useCase.updateFiles()
        waitCoroutines()

        assertEquals(Destination.AdvicesNoFiles, state.destination)
    }

    private fun TestScope.assertHasFiles(useCase: DuplicatesUseCasesImpl) {
        every { permissions.hasStoragePermissions } returns true

        useCase.updateFiles()
        waitCoroutines()

        assertEquals(Destination.List, state.destination)
    }

    private fun TestScope.assertHasNotPermissions() {
        every { permissions.hasStoragePermissions } returns false

        waitCoroutines()

        assertEquals(Destination.Permission, state.destination)
    }

    private suspend fun TestScope.assertProgressChanging(
        useCase: DuplicatesUseCasesImpl,
    ) {
        useCase.updateFiles()
        delay(1)
        assertTrue(state.isInProgress)
        waitCoroutines()
        assertFalse(state.isInProgress)
    }

    @Test
    fun switchGroupSelection() = runTest{
        useCases.switchGroupSelection(0)

        state.apply {
            assertTrue(selected.isNotEmpty())
            assertTrue(selected[0]!!.containsAll(duplicatesLists[0].imageInfos))

            useCases.switchGroupSelection(0)

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun switchItemSelection() = runTest {
        useCases.switchItemSelection(0, FIRST_FILE)
        useCases.switchItemSelection(0, SECOND_FILE)

        state.apply {
            assertTrue(selected[0]!!.containsAll(duplicatesLists[0].imageInfos))

            useCases.switchItemSelection(0, FIRST_FILE)

            assertFalse(isItemSelected(0, FIRST_FILE))
            assertTrue(isItemSelected(0, SECOND_FILE))

            useCases.switchItemSelection(0, SECOND_FILE)

            assertNull(selected[0])
            assertTrue(selected.isEmpty())
        }
    }



    @Test
    fun navigate() = runTest{
        Destination.values().forEach {
            useCases.navigate(it)
            assertEquals(it, state.destination)
        }
    }



    @Test
    fun `closeInter with item selection`() = runTest{
        state.uniteWay = UniteWay.Selected
        useCases.closeInter()
        assertEquals(Destination.AskContinue, state.destination)

        state.uniteWay = UniteWay.All
        useCases.closeInter()
        assertEquals(Destination.AdvicesUnited, state.destination)
    }



    @Test
    fun continueUniting(){
        useCases.continueUniting()
        assertEquals(Destination.List, state.destination)
    }

    @Test
    fun completeUniting() {
        useCases.completeUniting()
        assertEquals(Destination.Advices, state.destination)
    }

    @Test
    fun unite() = runTest{
        useCases.unite()
        coVerify { uniteUseCase.unite() }
    }

    @Test
    fun close() = runTest{
        useCases.close()

        assertTrue(state.isClosed)
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