package yin_kio.duplicates.domain

import io.mockk.coEvery
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
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    private lateinit var state: MutableStateHolder
    private val coroutineScope = CoroutineScope(StandardTestDispatcher())

    @BeforeEach
    fun setup(){
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
            assertTrue(selected.containsAll(duplicatesList[0]))

            useCase.switchGroupSelection(0)

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun switchItemSelection() = runTest {
        val useCase = duplicatesUseCase()
        wait()

        useCase.switchItemSelection(0, "a")
        useCase.switchItemSelection(0, "b")

        state.apply {
            assertTrue(selected.containsAll(duplicatesList[0]))

            useCase.switchItemSelection(0, "a")

            assertFalse(selected.contains(ImageInfo("a")))
            assertTrue(selected.contains(ImageInfo("b")))

            useCase.switchItemSelection(0, "b")

            assertTrue(selected.isEmpty())
        }
    }

    @Test
    fun isItemSelected() = runTest{
        val useCase = duplicatesUseCase()
        wait()

        useCase.switchItemSelection(0, "a")
        assertTrue(useCase.isItemSelected("a"))

        useCase.switchItemSelection(0, "a")
        assertFalse(useCase.isItemSelected("a"))
    }


    private suspend fun TestScope.duplicatesUseCase(): DuplicatesUseCase {
        val imagesComparator: (ImageInfo, ImageInfo) -> Boolean = { a, b -> true }
        val files: Files = mockk()
        coEvery { files.getImages() } returns listOf(ImageInfo("a"), ImageInfo("b")).also { delay(50) }

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

}