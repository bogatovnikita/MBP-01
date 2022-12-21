package yin_kio.file_manager.domain

data class FileInfo(
    val name: String = "",
    val time: Long = 0,
    val size: Long = 0,
    var isSelected: Boolean = false,
    val path: String = ""
)