package yin_kio.duplicates.presentation.models

data class Image(
    val path: String = "",
    val isSelected: Boolean = false,
    val groupId: Int = -1
)