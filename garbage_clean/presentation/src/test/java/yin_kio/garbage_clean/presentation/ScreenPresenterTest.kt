package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.UiDeleteForm
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo


@RunWith(RobolectricTestRunner::class)
class ScreenPresenterTest {


    private val context: Context = RuntimeEnvironment.getApplication()
    private val viewModel: GarbageCleanViewModel = spyk()
    private val presenter = ScreenPresenter(
        context, viewModel
    )



    @Test
    fun `test outFileSystemInfo`() {
        val input = FileSystemInfo(
            occupied = 10,
            available = 1_000_000,
            total = 1_000_000_000,
        )
        val output = UiFileSystemInfo(
            occupied = formatFileSize(context, input.occupied),
            available = formatFileSize(context, input.available),
            total = formatFileSize(context, input.total)
        )

        presenter.outFileSystemInfo(input)

        verify { viewModel.setFileSystemInfo(output)  }

    }

    @Test
    fun `test outUpdateProgress`(){
        presenter.outUpdateProgress(true)
        verify { viewModel.isInProgress = true }

        presenter.outUpdateProgress(false)
        verify { viewModel.isInProgress = false }
    }

    @Test
    fun `test outHasPermission`(){
        presenter.outHasPermission(true)
        verify { viewModel.hasPermission = true }

        presenter.outHasPermission(false)
        verify { viewModel.hasPermission = false }
    }

    @Test
    fun `test outDeleteProgress`(){
        DeleteProgressState.values().forEach {
            presenter.outDeleteProgress(it)

            verify { viewModel.setDeleteProgress(it) }
        }
    }

    @Test
    fun `test outDeleteForm`(){
        val deleteFormOut = DeleteFormOut()
        presenter.outDeleteForm(deleteFormOut)
        verify { viewModel.setDeleteForm(
            UiDeleteForm(
                isAllSelected = deleteFormOut.isAllSelected,
                canFree = formatFileSize(context, 0),
                items = listOf()
        )) }
    }

}