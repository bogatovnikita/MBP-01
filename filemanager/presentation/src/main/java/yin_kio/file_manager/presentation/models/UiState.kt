package yin_kio.file_manager.presentation.models

import yin_kio.file_manager.domain.models.*

data class UiState(
    val fileRequest: FileRequest,
    val isAllSelected: Boolean,
    val listShowingMode: ListShowingMode,
    val listShowingModeIconRes: Int,
    val sortingIconColor: Int,
    val sortingMode: SortingMode,
    val deleteButtonBg: Int,
    val hasPermission: Boolean,
    val files: List<FileInfo>,
    val isShowSortingModeSelector: Boolean,
    val inProgress: Boolean,
    val progressAlpha: Float,
    val deleteState: DeleteState,
    val askDeleteTitle: String,
    val deleteProgressTitle: String,
    val isShowInter: Boolean,
    val doneTitle: String
)
