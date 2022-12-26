package yin_kio.file_manager.presentation.models

import androidx.recyclerview.widget.RecyclerView.LayoutManager
import yin_kio.file_manager.domain.models.FileMode

data class UiState(
    val isSortingModeEnabled: Boolean = false,
    val showingMode: LayoutManager,
    val isAllSelected: Boolean = false,
    val fileMode: FileMode
)
