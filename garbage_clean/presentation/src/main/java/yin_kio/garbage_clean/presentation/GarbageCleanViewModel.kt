package yin_kio.garbage_clean.presentation

import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.UiDeleteForm
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

interface GarbageCleanViewModel {

    fun setFileSystemInfo(uiFileSystemInfo: UiFileSystemInfo)
    var isInProgress: Boolean
    var hasPermission: Boolean
    fun setDeleteProgress(deleteProgressState: DeleteProgressState)
    fun setDeleteForm(deleteForm: UiDeleteForm)

}