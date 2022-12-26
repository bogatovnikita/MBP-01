package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_manager.domain.models.*

internal class FileManager(
    private val stateHolder: MutableStateHolder,
    private val permissionChecker: PermissionChecker,
    private val files: Files,
    private val coroutineScope: CoroutineScope
) {

    private val state = MutableState()

    init {
        state.sortingMode = SortingMode.Disabled
        updateState()

        updateFiles()
    }

    fun updateFiles() {
        coroutineScope.launch {
            state.apply {
                updateSelectAll(false)
                hasPermission = permissionChecker.hasPermission
                updateState()

                if (hasPermission) {
                    inProgress = true
                    updateState()
                    files = this@FileManager.files.getFiles(state.fileMode)
                    switchSortingMode(state.sortingMode)
                    inProgress = false
                } else {
                    state.inProgress = false
                }

                updateState()
            }
        }
    }



    fun switchFileMode(fileMode: FileMode){
        state.fileMode = fileMode
        updateState()
    }

    fun switchSortingMode(sortingMode: SortingMode){
        state.isShowSortingModeSelector = false
        state.sortingMode = sortingMode
        state.files = when(sortingMode){
            SortingMode.FromNewToOld ->  state.files.sortedBy { it.time }
            SortingMode.FromOldToNew ->  state.files.sortedBy { -it.time }
            SortingMode.FromBigToSmall -> state.files.sortedBy { -it.size }
            SortingMode.FromSmallToBig -> state.files.sortedBy { it.size }
            SortingMode.Disabled -> state.files
        }
        updateState()
    }

    fun switchSelectAll(){
        state.apply {
            updateSelectAll(!isAllSelected)
            updateCanDelete()
        }
        updateState()
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
        updateState()
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
        updateState()

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
        updateState()
    }

    fun delete(){
        state.isShowInter = true
        state.deleteState = DeleteState.Progress
        updateState()
        coroutineScope.launch {
            files.delete(state.selectedFiles.map { it.path })
            state.deleteState = DeleteState.Done
            updateState()
        }

    }

    fun askDelete(){
        state.deleteState = DeleteState.Ask
        updateState()
    }

    fun cancelDelete(){
        state.deleteState = DeleteState.Wait
        updateState()
    }

    fun hideInter(){
        state.isShowInter = false
        updateState()
    }

    fun completeDelete(){
        state.deleteState = DeleteState.Wait
        updateFiles()
    }

    fun showSortingModeSelector(){
        state.isShowSortingModeSelector = true
        updateState()
    }

    private fun updateState() {
        stateHolder.update(state.copy())
    }
}


