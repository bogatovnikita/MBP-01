package yin_kio.duplicates.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    private lateinit var state: MutableStateHolder
    private val coroutineScope = CoroutineScope(StandardTestDispatcher())
    private lateinit var files: Files

    @BeforeEach
    fun setup(){
        files = mockk()
        state = MutableStateHolder(coroutineScope)
    }

    @Test
    fun `init`() = runTest{

        duplicatesUseCase()
        state.apply {
            assertTrue(duplicatesList.isEmpty())
            assertTrue(isInProgress)
            wait()
            assertFalse(isInProgress)
            assertTrue(duplicatesList.isNotEmpty())
        }

    }

    @Test
    fun updateFiles() = runTest{
        state.apply {
            val oldDuplicates = duplicatesList

            val useCase = duplicatesUseCase()
            wait()

            useCase.updateFiles()
            delay(1)
            assertTrue(isInProgress)
            wait()
            assertFalse(isInProgress)
            assertFalse(duplicatesList === oldDuplicates)
        }
    }

    @Test
    fun switchGroupSelection() = runTest{
        val useCase = duplicatesUseCase()
        wait()

        useCase.switchGroupSelection(0)

        state.apply {
            assertTrue(selected.isNotEmpty())
            assertTrue(selected[0]!!.containsAll(duplicatesList[0]))

            useCase.switchGroupSelection(0)

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun switchItemSelection() = runTest {
        val useCase = duplicatesUseCase()
        wait()

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
        val useCase = duplicatesUseCase()
        wait()

        useCase.switchItemSelection(0, FIRST_FILE)
        assertTrue(useCase.isItemSelected(0 , FIRST_FILE))

        useCase.switchItemSelection(0, FIRST_FILE)
        assertFalse(useCase.isItemSelected(0 , FIRST_FILE))
    }

    @Test
    fun navigate() = runTest{
        val useCase = duplicatesUseCase()

        Destination.values().forEach {
            useCase.navigate(it)
            assertEquals(it, state.destination)
        }

    }


    @Test
    fun unite() = runTest{
        val destination = "gallery"

        val useCase = duplicatesUseCase()
        wait()

        useCase.switchGroupSelection(0)
        every { files.unitedDestination() } returns destination

        mockAndVerify(
            returned = Unit,
            mocked = arrayOf(
                { files.copy(FIRST_FILE, destination) },
                { files.delete(FIRST_FILE) },
                { files.delete(SECOND_FILE) }
            ),
            action = {
                useCase.unite()
                wait()
            }
        )
    }

    private fun <T> mockAndVerify(
        mocked: Array<suspend () -> T>,
        action: () -> T,
        returned: T
    ){
        mocked.forEach { coEvery { it() } returns returned }

        action()

        mocked.forEach { coVerify { it() } }
    }


    private suspend fun TestScope.duplicatesUseCase(): DuplicatesUseCase {
        val imagesComparator: (ImageInfo, ImageInfo) -> Boolean = { a, b -> true }
        coEvery { files.getImages() } returns listOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE)).also { delay(50) }

        return DuplicatesUseCase(
            state = state,
            files = files,
            imagesComparator = imagesComparator,
            coroutineScope = coroutineScope,
            coroutineContext = coroutineContext
        )
    }

    private fun TestScope.wait(){
        advanceUntilIdle()
    }

    companion object{
        private const val FIRST_FILE = "a"
        private const val SECOND_FILE = "b"
    }

}