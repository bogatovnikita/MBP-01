package yin_kio.file_manager.presentation

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.file_manager.domain.models.FileGroup
import yin_kio.file_manager.domain.models.FileInfo

@RunWith(RobolectricTestRunner::class)
internal class DescriptionPresenterTest{

    @Test
    fun `test present`() {
        presenter().apply {

            assertEquals("0 B • path", present(simpleFile(size = 0)))
            assertEquals("1 B • path", present(simpleFile(size = 1)))
            assertEquals("1.00 kB • path", present(simpleFile(size = 1000)))
            assertEquals("1.00 MB • path", present(simpleFile(size = 1000_000)))
            assertEquals("1.00 GB • path", present(simpleFile(size = 1000_000_000)))
            assertEquals("00:00 • 0 B • path", present(audioFile()))

        }
    }

    private fun presenter(): DescriptionPresenter {
        val context = RuntimeEnvironment.getApplication()
        return DescriptionPresenter(context)
    }

    private fun simpleFile(size: Long) = FileInfo(
        path = "path",
        size = size
    )

    private fun audioFile() = FileInfo(
        fileGroup = FileGroup.Audio,
        path = "path"
    )

}