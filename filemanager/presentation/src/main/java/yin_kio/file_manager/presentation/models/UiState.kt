package yin_kio.file_manager.presentation.models

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import yin_kio.file_manager.domain.models.FileRequest

data class UiState(
    val fileRequest: FileRequest,
    val isAllSelected: Boolean,
    val layoutManager: LayoutManager,
    val sortingIconColor: Int,
    val deleteButtonColor: Int
)
