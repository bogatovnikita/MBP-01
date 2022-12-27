package yin_kio.file_manager.presentation.models

import androidx.recyclerview.widget.RecyclerView.LayoutManager
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileRequest

data class UiState(
    val fileRequest: FileRequest,
    val isAllSelected: Boolean,
    val layoutManager: LayoutManager,
    val sortingIconColor: Int,
    val deleteButtonBg: Int,
    val hasPermission: Boolean,
    val files: List<FileInfo>
)
