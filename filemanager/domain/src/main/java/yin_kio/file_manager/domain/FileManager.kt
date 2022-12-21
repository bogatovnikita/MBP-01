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

    fun switchSelectAll(){
        state.apply {
            isAllSelected = !isAllSelected
            files.forEach{
                it.isSelected = isAllSelected
            }
            selectedFiles = if (isAllSelected) files.toMutableList() else mutableListOf()

            updateCanDelete()
        }
    }



    fun switchShowingMode(){
        state.showingMode = when(state.showingMode){
            ShowingMode.Grid -> ShowingMode.List
            ShowingMode.List -> ShowingMode.Grid
        }
    }

    fun switchSelectFile(path: String){
        state.apply {
            val file = files.find { it.path == path }
            file?.let {
                it.isSelected = !it.isSelected
                addOrRemoveFromSelectedFiles(it)
                isAllSelected = files == selectedFiles
            }
            updateCanDelete()
        }

    }

    private fun MutableState.updateCanDelete() {
        canDelete = selectedFiles.isNotEmpty()
    }

    private fun MutableState.addOrRemoveFromSelectedFiles(it: FileInfo) {
        if (it.isSelected) {
            selectedFiles.add(it)
        } else {
            selectedFiles.remove(it)
        }
    }

    fun goBack(){
        state.isShouldGoBack = true
    }
}


