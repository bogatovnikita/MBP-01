package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.presentation.models.UiDeleteForm
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

class Presenter(
    private val context: Context,
    private val viewModel: GarbageCleanViewModel
) : OutBoundary {

    private val iconPresenter = DeleteFormItemPresenter(context)

    override fun outUpdateProgress(isInProgress: Boolean) {
        viewModel.isInProgress = isInProgress
    }

    override fun outDeleteForm(deleteFormOut: DeleteFormOut) {
        viewModel.setDeleteForm(
            UiDeleteForm(
                isAllSelected = deleteFormOut.isAllSelected,
                canFree = formatFileSize(context, deleteFormOut.items.sumOf { it.size }),
                items = deleteFormOut.items.map { UiDeleteFromItem(
                    iconRes = iconPresenter.presentIcon(it.garbageType),
                    name = iconPresenter.presentName(it.garbageType),
                    size = formatFileSize(context, it.size)
                ) }
            )
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

    override fun outHasPermission(has: Boolean) {
        viewModel.hasPermission = has
    }

    override fun outDeleteProgress(deleteProgressState: DeleteProgressState) {
        viewModel.setDeleteProgress(deleteProgressState)
    }
}