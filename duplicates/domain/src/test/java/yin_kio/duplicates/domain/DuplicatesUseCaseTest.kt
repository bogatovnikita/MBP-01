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
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.State


@OptIn(ExperimentalCoroutinesApi::class)
class DuplicatesUseCaseTest {

    @Test
    fun `test init`() = runTest{
        val imagesComparator: (ImageInfo, ImageInfo) -> Boolean = {a,b -> true}
        val stateFlow = MutableStateFlow(State())
        val files: Files = mockk()
        coEvery { files.getImages() } returns listOf(ImageInfo(), ImageInfo()).also { delay(50) }

        DuplicatesUseCase(
            stateFlow = stateFlow,
            files = files,
            imagesComparator = imagesComparator,
            coroutineScope = this,
            coroutineContext = coroutineContext
        )

        assertTrue(stateFlow.value.duplicatesList.value.isEmpty())
        assertTrue(stateFlow.value.isInProgress)
        wait()
        assertFalse(stateFlow.value.isInProgress)
        assertTrue(stateFlow.value.duplicatesList.value.isNotEmpty())
    }

    private fun TestScope.wait(){
        advanceUntilIdle()
    }

}