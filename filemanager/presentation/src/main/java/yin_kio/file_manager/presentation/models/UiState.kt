package yin_kio.file_manager.presentation.models

import yin_kio.file_manager.domain.models.FileRequest

data class UiState(
    val fileRequest: FileRequest,
    val isAllSelected: Boolean
)
