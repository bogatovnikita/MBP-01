package yin_kio.duplicates.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.State


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    private lateinit var stateFlow: MutableStateFlow<State>

    @BeforeEach
    fun setup(){
        stateFlow = MutableStateFlow(State())
    }

    @Test
    fun `test init`() = runTest{

        duplicatesUseCase(stateFlow)

        assertTrue(stateFlow.value.duplicatesList.value.isEmpty())
        assertTrue(stateFlow.value.isInProgress)
        wait()
        assertFalse(stateFlow.value.isInProgress)
        assertTrue(stateFlow.value.duplicatesList.value.isNotEmpty())
    }

    @Test
    fun `test updateFiles`() = runTest{
        val oldDuplicates = stateFlow.value.duplicatesList

        val useCase = duplicatesUseCase(stateFlow)
        wait()

        useCase.updateFiles()
        delay(1)
        assertTrue(stateFlow.value.isInProgress)
        wait()
        assertFalse(stateFlow.value.isInProgress)
        assertFalse(stateFlow.value.duplicatesList === oldDuplicates)
    }

    private suspend fun TestScope.duplicatesUseCase(stateFlow: MutableStateFlow<State>): DuplicatesUseCase {
        val imagesComparator: (ImageInfo, ImageInfo) -> Boolean = { a, b -> true }
        val files: Files = mockk()
        coEvery { files.getImages() } returns listOf(ImageInfo(), ImageInfo()).also { delay(50) }

        return DuplicatesUseCase(
            stateFlow = stateFlow,
            files = files,
            imagesComparator = imagesComparator,
            coroutineScope = this,
            coroutineContext = coroutineContext
        )
    }

    private fun TestScope.wait(){
        advanceUntilIdle()
    }

}