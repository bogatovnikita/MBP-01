package yin_kio.file_manager.domain.models

internal data class MutableState(
    override var hasPermission: Boolean = false,
    override var inProgress: Boolean = true,
    override var files: List<FileInfo> = listOf(),
    override var fileMode: FileMode = FileMode.Images,
    override var sortingMode: SortingMode = SortingMode.FromNewToOld,
    override var selectedFiles: MutableList<FileInfo> = mutableListOf(),
    override var isAllSelected: Boolean = false,
    override var listShowingMode: ListShowingMode = ListShowingMode.List,

    override var isShouldGoBack: Boolean = false,

    override var canDelete: Boolean = false,
    override var isShowInter: Boolean = false,

    override var deleteState: DeleteState = DeleteState.Wait,
    override var isShowSortingModeSelector: Boolean = false
) : State