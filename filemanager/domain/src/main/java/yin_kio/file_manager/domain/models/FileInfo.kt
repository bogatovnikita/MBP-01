package yin_kio.file_manager.domain.models

data class FileInfo(
    val fileGroup: FileGroup = FileGroup.Unknown,
    val name: String = "",
    val description: String = "",
    val time: Long = 0,
    val size: Long = 0,
    var isSelected: Boolean = false,
    val path: String = ""
)