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
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    private lateinit var state: MutableStateHolder
    private val dispatcher = StandardTestDispatcher()
    private val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var files: Files
    private lateinit var permissions: Permissions
    private lateinit var useCase: DuplicatesUseCase

    @BeforeEach
    fun setup() = runTest{
        files = mockk()
        permissions = mockk()
        state = MutableStateHolder(coroutineScope)

        every { permissions.hasStoragePermissions } returns true
        useCase = createDuplicatesUseCase()
        waitCoroutines()
    }

    @Test
    fun `init with has permission`() = runTest{
        val state = MutableStateHolder(this)
        createDuplicatesUseCase(stateHolder = state)
        state.apply {
            assertTrue(duplicatesList.isEmpty())
            assertTrue(isInProgress)
            assertEquals(Destination.List, state.destination)
            waitCoroutines()
            assertFalse(isInProgress)
            assertTrue(duplicatesList.isNotEmpty())
            assertEquals(Destination.List, state.destination)
        }
    }

    @Test
    fun `init with has not permission`() = runTest {
        every { permissions.hasStoragePermissions } returns false

        createDuplicatesUseCase()
        waitCoroutines()

        assertEquals(Destination.Permission, state.destination)
    }

    @Test
    fun updateFiles() = runTest{
        state.apply {
            val oldDuplicates = duplicatesList

            val useCase = createDuplicatesUseCase()
            waitCoroutines()

            useCase.updateFiles()
            delay(1)
            assertTrue(isInProgress)
            waitCoroutines()
            assertFalse(isInProgress)
            assertFalse(duplicatesList === oldDuplicates)
            assertEquals(Destination.List, state.destination)
        }
    }

    @Test
    fun switchGroupSelection() = runTest{
        useCase.selectGroup(0)

        state.apply {
            assertTrue(selected.isNotEmpty())
            assertTrue(selected[0]!!.containsAll(duplicatesList[0]))

            useCase.selectGroup(0)

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun switchItemSelection() = runTest {
        useCase.switchItemSelection(0, FIRST_FILE)
        useCase.switchItemSelection(0, SECOND_FILE)

        state.apply {
            assertTrue(selected[0]!!.containsAll(duplicatesList[0]))

            useCase.switchItemSelection(0, FIRST_FILE)

            assertFalse(selected[0]!!.contains(ImageInfo(FIRST_FILE)))
            assertTrue(selected[0]!!.contains(ImageInfo(SECOND_FILE)))

            useCase.switchItemSelection(0, SECOND_FILE)

            assertNull(selected[0])
        }
    }

    @Test
    fun isItemSelected() = runTest{
        useCase.switchItemSelection(0, FIRST_FILE)
        assertTrue(useCase.isItemSelected(0 , FIRST_FILE))

        useCase.switchItemSelection(0, FIRST_FILE)
        assertFalse(useCase.isItemSelected(0 , FIRST_FILE))
    }

    @Test
    fun navigate() = runTest{
        Destination.values().forEach {
            useCase.navigate(it)
            assertEquals(it, state.destination)
        }
    }


    @Test
    fun uniteSelected() = runTest{
        val folderForUnited = "gallery"
        mockFilesCalls(folderForUnited)

        val useCase = createDuplicatesUseCase()
        waitCoroutines()

        useCase.selectGroup(0)

        useCase.uniteSelected()

        assertEquals(Destination.UniteProgress, state.destination)
        waitCoroutines()
        assertEquals(Destination.Inter, state.destination)

        coVerify { files.copy(FIRST_FILE, folderForUnited) }
        coVerify { files.delete(FIRST_FILE) }
        coVerify { files.delete(SECOND_FILE) }
    }

    private fun mockFilesCalls(destination: String) {
        every { files.folderForUnited() } returns destination

        coEvery { files.copy(FIRST_FILE, destination) } returns Unit
        coEvery { files.delete(FIRST_FILE) } returns Unit
        coEvery { files.delete(SECOND_FILE) } returns Unit
    }

    private suspend fun TestScope.createDuplicatesUseCase(stateHolder: MutableStateHolder = state): DuplicatesUseCase {
        coEvery { files.getImages() } returns listOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE)).also { delay(50) }

        return DuplicatesUseCase(
            state = stateHolder,
            files = files,
            imagesComparator = imagesComparator(),
            permissions = permissions,
            coroutineScope = coroutineScope,
            coroutineContext = coroutineContext
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