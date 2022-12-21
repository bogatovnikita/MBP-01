package yin_kio.file_manager.domain.models

data class FileInfo(
    val time: Long = 0,
    val size: Long = 0,
    var isSelected: Boolean = false,
    val path: String = ""
)