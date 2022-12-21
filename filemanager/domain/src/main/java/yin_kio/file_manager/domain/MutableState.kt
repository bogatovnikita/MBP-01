package yin_kio.file_manager.domain

data class MutableState(
    var hasPermission: Boolean = false,
    var inProgress: Boolean = true,
    var files: List<FileInfo> = listOf(),
    var fileMode: FileMode = FileMode.Images,
    var sortingMode: SortingMode = SortingMode.FromNewToOld,
    var selectedFiles: List<FileInfo> = listOf(),
    var isAllSelected: Boolean = false,
    var showingMode: ShowingMode = ShowingMode.List
)