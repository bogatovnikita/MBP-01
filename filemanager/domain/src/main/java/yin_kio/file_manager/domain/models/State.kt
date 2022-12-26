package yin_kio.file_manager.domain.models

interface State {
    val hasPermission: Boolean
    val inProgress: Boolean
    val files: List<FileInfo>
    val fileRequest: FileRequest
    val sortingMode: SortingMode
    val selectedFiles: MutableList<FileInfo>
    val isAllSelected: Boolean
    val listShowingMode: ListShowingMode

    val isShouldGoBack: Boolean

    val canDelete: Boolean
    val isShowInter: Boolean

    val deleteState: DeleteState
    val isShowSortingModeSelector: Boolean
}