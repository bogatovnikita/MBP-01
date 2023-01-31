package yin_kio.garbage_clean.presentation.models

data class ScreenState(
    val isInProgress: Boolean,
    val buttonState: ButtonState,
    val deleteForm: UiDeleteForm,
    val fileSystemInfo: UiFileSystemInfo
)
