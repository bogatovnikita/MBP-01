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

        assertTrue(state.duplicatesList.isEmpty())
        assertTrue(state.isInProgress)
        wait()
        assertFalse(state.isInProgress)
        assertTrue(state.duplicatesList.isNotEmpty())
    }

    @Test
    fun updateFiles() = runTest{
        val oldDuplicates = state.duplicatesList

        val useCase = duplicatesUseCase()
        wait()

        useCase.updateFiles()
        delay(1)
        assertTrue(state.isInProgress)
        wait()
        assertFalse(state.isInProgress)
        assertFalse(state.duplicatesList === oldDuplicates)
    }

    @Test
    fun switchGroupSelection() = runTest{
        val useCase = duplicatesUseCase()
        wait()

        useCase.switchGroupSelection(0)

        assertTrue(state.selected[0]?.isNotEmpty() == true)
        assertEquals(state.duplicatesList[0].size, state.selected[0]?.size)

        useCase.switchGroupSelection(0)

        assertTrue(state.selected.isEmpty())

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