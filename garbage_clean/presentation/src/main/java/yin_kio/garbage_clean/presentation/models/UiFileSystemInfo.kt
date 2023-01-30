package yin_kio.garbage_clean.presentation.models

data class UiFileSystemInfo(
    val isInProgress: Boolean = true,
    val occupied: String = "",
    val available: String = "",
    val total: String = ""
)