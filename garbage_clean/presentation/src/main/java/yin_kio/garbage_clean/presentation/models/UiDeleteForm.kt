package yin_kio.garbage_clean.presentation.models

data class UiDeleteForm(
    val isAllSelected: Boolean,
    val canFree: String,
    val items: List<UiDeleteFromItem>
)