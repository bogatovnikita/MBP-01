package yin_kio.garbage_clean.presentation.presenter

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo
import yin_kio.garbage_clean.presentation.view_model.MutableScreenViewModel

class ScreenPresenter(
    private val context: Context,
) : OutBoundary {

    var viewModel: MutableScreenViewModel? = null

    private val screenItemsPresenter = ScreenItemsPresenter(context)

    override fun outUpdateProgress(isInProgress: Boolean) {
        viewModel?.setIsInProgress(isInProgress)
    }

    override fun outDeleteForm(deleteFormOut: DeleteFormOut) {
        viewModel?.apply {
            setDeleteFormItems(uiDeleteFromItems(deleteFormOut))
            setCanFreeSize(screenItemsPresenter.presentCanFree(deleteFormOut.items.sumOf { it.size }))
            setIsAllSelected(deleteFormOut.isAllSelected)
            setButtonText(screenItemsPresenter.presentButtonName(deleteFormOut.items.isEmpty()))
            setButtonBgRes(screenItemsPresenter.presentButtonBg(deleteFormOut.canDelete))
        }
    }

    private fun uiDeleteFromItems(deleteFormOut: DeleteFormOut) =
        deleteFormOut.items.map {
            UiDeleteFromItem(
                iconRes = screenItemsPresenter.presentIcon(it.garbageType),
                name = screenItemsPresenter.presentName(it.garbageType),
                size = formatFileSize(context, it.size),
                isSelected =  it.isSelected
            )
        }


    override fun outFileSystemInfo(fileSystemInfo: FileSystemInfo) {
        viewModel?.setFileSystemInfo(
            UiFileSystemInfo(
                occupied = formatFileSize(context, fileSystemInfo.occupied),
                available = formatFileSize(context, fileSystemInfo.available),
                total = formatFileSize(context, fileSystemInfo.total)
            )
        )
    }

    override fun outHasPermission(hasPermission: Boolean) {
        viewModel?.setHasPermission(hasPermission)
    }

    override fun outDeleteProgress(deleteProgressState: DeleteProgressState) {
        viewModel?.setDeleteProgress(deleteProgressState)
    }
}