package yin_kio.duplicates.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.use_cases.DuplicateRemover
import yin_kio.duplicates.domain.use_cases.DuplicateRemoverImpl
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.models.ImageInfo

@OptIn(ExperimentalCoroutinesApi::class)
class DuplicateRemoverTest {

    @Test
    fun testRemove() = runTest{
        val files: Files = mockk()
        every { files.folderForUnited() } returns GALLERY_FOLDER

        coEvery { files.copy(FIRST_FILE, GALLERY_FOLDER) } returns Unit
        coEvery { files.copy(SECOND_FILE, GALLERY_FOLDER) } returns Unit
        coEvery { files.delete(FIRST_FILE) } returns Unit
        coEvery { files.delete(SECOND_FILE) } returns Unit


        val remover: DuplicateRemover = DuplicateRemoverImpl(files)

        remover.invoke(
            listOf(
                listOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE))
            )
        )

        coVerify { files.copy(FIRST_FILE, GALLERY_FOLDER) }
        coVerify(inverse = true) { files.copy(SECOND_FILE, GALLERY_FOLDER) }
        coVerify { files.delete(FIRST_FILE) }
        coVerify { files.delete(SECOND_FILE) }
    }

    companion object{
        private const val FIRST_FILE = "a"
        private const val SECOND_FILE = "b"
        private const val GALLERY_FOLDER = "gallery"
    }

}