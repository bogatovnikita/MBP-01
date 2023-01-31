package yin_kio.garbage_clean.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

class ScreenViewModel : MutableScreenViewModel {


    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    override fun setFileSystemInfo(uiFileSystemInfo: UiFileSystemInfo) {
        _state.value = state.value.copy(
            fileSystemInfo = uiFileSystemInfo
        )
    }

    override fun setIsInProgress(isInProgress: Boolean) {
        _state.value = state.value.copy(isInProgress = isInProgress)
    }

    override fun setHasPermission(hasPermission: Boolean) {
        _state.value = state.value.copy(hasPermission = hasPermission)
    }

    override fun setDeleteProgress(deleteProgressState: DeleteProgressState) {
        _state.value = state.value.copy(deleteProgressState = deleteProgressState)
    }

    override fun setDeleteFormItems(deleteFormItems: List<UiDeleteFromItem>) {
        _state.value = state.value.copy(deleteFormItems = deleteFormItems)
    }

    override fun setIsAllSelected(isAllSelected: Boolean) {
        _state.value = state.value.copy(isAllSelected = isAllSelected)
    }

    override fun setCanFreeVolume(canFreeVolume: String) {
        _state.value = state.value.copy(canFreeVolume = canFreeVolume)
    }

    override fun setButtonText(text: String) {
        _state.value = state.value.copy(buttonText = text)
    }

    override fun setButtonBgRes(bgRes: Int) {
        _state.value = state.value.copy(buttonBgRes = bgRes)
    }
}