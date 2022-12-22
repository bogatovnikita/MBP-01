package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_manager.domain.models.*

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
                updateSelectAll(false)
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
        state.isShowSortingModeSelector = false
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
            updateSelectAll(!isAllSelected)
            updateCanDelete()
        }
    }

    private fun MutableState.updateSelectAll(isAllSelected: Boolean) {
        this.isAllSelected = isAllSelected
        files.forEach {
            it.isSelected = isAllSelected
        }
        selectedFiles = if (isAllSelected) files.toMutableList() else mutableListOf()
    }


    fun switchShowingMode(){
        state.listShowingMode = when(state.listShowingMode){
            ListShowingMode.Grid -> ListShowingMode.List
            ListShowingMode.List -> ListShowingMode.Grid
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

    fun delete(){
        state.isShowInter = true
        state.deleteState = DeleteState.Progress
        coroutineScope.launch {
            files.delete(state.selectedFiles.map { it.path })
            state.deleteState = DeleteState.Done
        }
    }

    fun askDelete(){
        state.deleteState = DeleteState.Ask
    }

    fun cancelDelete(){
        state.deleteState = DeleteState.Wait
    }

    fun hideInter(){
        state.isShowInter = false
    }

    fun completeDelete(){
        state.deleteState = DeleteState.Wait
        updateFiles()
    }

    fun showSortingModeSelector(){
        state.isShowSortingModeSelector = true
    }
}


