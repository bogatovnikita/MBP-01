package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FileManager(
    private val state: MutableState,
    private val permissionChecker: PermissionChecker,
    private val files: Files,
    private val coroutineScope: CoroutineScope
) {

    init {
        updateFiles()
    }

    fun updateFiles() {
        coroutineScope.launch {
            state.apply {
                hasPermission = permissionChecker.hasPermission
                if (hasPermission) {
                    inProgress = true
                    files = this@FileManager.files.getFiles(state.fileMode)
                    inProgress = false
                } else {
                    state.inProgress = false
                }
            }
        }
    }

    fun switchFileMode(fileMode: FileMode){
        state.fileMode = fileMode
    }

    fun switchSortingMode(sortingMode: SortingMode){
        state.sortingMode = sortingMode
        state.files = when(sortingMode){
            SortingMode.FromNewToOld ->  state.files.sortedBy { it.time }
            SortingMode.FromOldToNew ->  state.files.sortedBy { -it.time }
            SortingMode.FromBigToSmall -> state.files.sortedBy { -it.size }
            SortingMode.FromSmallToBig -> state.files.sortedBy { it.size }
        }
    }


}