package yin_kio.garbage_clean.presentation

import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

interface GarbageCleanViewModel {

    fun setFileSystemInfo(uiFileSystemInfo: UiFileSystemInfo)
    fun setIsInProgress(isInProgress: Boolean)
    fun setHasPermission(hasPermission: Boolean)
    fun setDeleteProgress(deleteProgressState: DeleteProgressState)
    fun setDeleteFormItems(deleteFormItems: List<UiDeleteFromItem>)
    fun setIsAllSelected(isAllSelected: Boolean)
    fun setCanFreeVolume(canFreeVolume: String)
    fun setButtonText(text: String)
    fun setButtonBgRes(bgRes: Int)

}