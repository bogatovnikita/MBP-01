package yin_kio.file_manager.presentation.models

data class FileItem(
    val name: String,
    val description: String,
    val isSelected: Boolean,
    val path: String
)
