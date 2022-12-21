package yin_kio.file_manager.domain.models

data class MutableState(
    var hasPermission: Boolean = false,
    var inProgress: Boolean = true,
    var files: List<FileInfo> = listOf(),
    var fileMode: FileMode = FileMode.Images,
    var sortingMode: SortingMode = SortingMode.FromNewToOld,
    var selectedFiles: MutableList<FileInfo> = mutableListOf(),
    var isAllSelected: Boolean = false,
    var listShowingMode: ListShowingMode = ListShowingMode.List,

    var isShouldGoBack: Boolean = false,

    var canDelete: Boolean = false,
    var isShowInter: Boolean = false,

    var deleteState: DeleteState = DeleteState.Wait
)