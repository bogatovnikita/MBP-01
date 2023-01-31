package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

class ScreenPresenter(
    private val context: Context,
    private val viewModel: MutableScreenViewModel
) : OutBoundary {

    private val screenItemsPresenter = ScreenItemsPresenter(context)

    override fun outUpdateProgress(isInProgress: Boolean) {
        viewModel.setIsInProgress(isInProgress)
    }

    override fun outDeleteForm(deleteFormOut: DeleteFormOut) {
        viewModel.setDeleteFormItems(uiDeleteFromItems(deleteFormOut))
        viewModel.setCanFreeVolume(formatFileSize(context, deleteFormOut.items.sumOf { it.size }))
        viewModel.setIsAllSelected(deleteFormOut.isAllSelected)
        viewModel.setButtonText(screenItemsPresenter.presentButtonName(deleteFormOut.items.isEmpty()))
        viewModel.setButtonBgRes(screenItemsPresenter.presentButtonBg(deleteFormOut.canDelete))
    }

    private fun uiDeleteFromItems(deleteFormOut: DeleteFormOut) =
        deleteFormOut.items.map {
            UiDeleteFromItem(
                iconRes = screenItemsPresenter.presentIcon(it.garbageType),
                name = screenItemsPresenter.presentName(it.garbageType),
                size = formatFileSize(context, it.size)
            )
        }


    override fun outFileSystemInfo(fileSystemInfo: FileSystemInfo) {
        viewModel.setFileSystemInfo(
            UiFileSystemInfo(
                occupied = formatFileSize(context, fileSystemInfo.occupied),
                available = formatFileSize(context, fileSystemInfo.available),
                total = formatFileSize(context, fileSystemInfo.total)
            )
        )
    }

    override fun outHasPermission(hasPermission: Boolean) {
        viewModel.setHasPermission(hasPermission)
    }

    override fun outDeleteProgress(deleteProgressState: DeleteProgressState) {
        viewModel.setDeleteProgress(deleteProgressState)
    }
}