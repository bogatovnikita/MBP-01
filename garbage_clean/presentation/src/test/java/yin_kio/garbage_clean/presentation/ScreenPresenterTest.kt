package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo


@RunWith(RobolectricTestRunner::class)
class ScreenPresenterTest {


    private val context: Context = RuntimeEnvironment.getApplication()
    private val viewModel: MutableScreenViewModel = spyk()
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
        verify { viewModel.setIsInProgress(true) }

        presenter.outUpdateProgress(false)
        verify { viewModel.setIsInProgress(false) }
    }

    @Test
    fun `test outHasPermission`(){
        presenter.outHasPermission(true)
        verify { viewModel.setHasPermission(true) }

        presenter.outHasPermission(false)
        verify { viewModel.setHasPermission(false) }
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
        verify {
            viewModel.setDeleteFormItems(emptyList())
            viewModel.setCanFreeVolume(formatFileSize(context, 0))
            viewModel.setIsAllSelected(deleteFormOut.isAllSelected)
            viewModel.setButtonText(context.getString(R.string.go_to_main_screen))
            viewModel.setButtonBgRes(general.R.drawable.bg_main_button_disabled)
        }
    }

}