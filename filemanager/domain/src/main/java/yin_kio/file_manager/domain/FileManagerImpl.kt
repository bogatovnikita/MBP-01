package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_manager.domain.models.*
import kotlin.coroutines.CoroutineContext

internal class FileManagerImpl(
    private val _stateHolder: MutableStateHolder,
    private val permissionChecker: PermissionChecker,
    private val files: Files,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) : FileManager {

    private val state = MutableState()
    override val stateHolder: StateHolder get() =  _stateHolder

    init {
        state.sortingMode = SortingMode.Disabled
        updateState()
        updateFiles()
    }

    override fun updateFiles() {
        asynchronous { suspendedUpdateFiles() }
    }

    private fun asynchronous(action: suspend CoroutineScope.() -> Unit){
        coroutineScope.launch(coroutineContext, block =  action)
    }

    private suspend fun suspendedUpdateFiles() {
        state.apply {
            version += 1
            files = emptyList()
            updateSelectAll(false)
            updateCanDelete()
            hasPermission = permissionChecker.hasPermission


            if (hasPermission) {
                inProgress = true
                updateState()
                delay(1)
                files = this@FileManagerImpl.files.getFiles(state.fileRequest)

                println("update")
                setSortingMode(state.sortingMode)
                inProgress = false
            } else {
                state.inProgress = false
            }

            updateState()
        }
    }


    override fun switchFileMode(fileRequest: FileRequest){
        asynchronous {
            state.fileRequest = fileRequest
            updateState()
            delay(50)
            suspendedUpdateFiles()
        }

    }

    override fun switchSortingMode(sortingMode: SortingMode){
        setSortingMode(sortingMode)
        updateState()
    }

    private fun setSortingMode(sortingMode: SortingMode) {
        state.isShowSortingModeSelector = false
        state.sortingMode = sortingMode
        state.files = when (sortingMode) {
            SortingMode.FromNewToOld -> state.files.sortedBy { it.time }
            SortingMode.FromOldToNew -> state.files.sortedBy { -it.time }
            SortingMode.FromBigToSmall -> state.files.sortedBy { -it.size }
            SortingMode.FromSmallToBig -> state.files.sortedBy { it.size }
            SortingMode.Disabled -> state.files
        }
    }

    override fun switchSelectAll(){
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


    override fun switchShowingMode(){
        state.listShowingMode = when(state.listShowingMode){
            ListShowingMode.Grid -> ListShowingMode.List
            ListShowingMode.List -> ListShowingMode.Grid
        }
        updateState()
    }

    override fun switchSelectFile(path: String){
        state.apply {
            version += 1
            val file = files.find { it.path == path }

            file?.let {
                it.isSelected = !it.isSelected
                addOrRemoveFromSelectedFiles(it)
                isAllSelected = files.size == selectedFiles.size
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

    override fun goBack(){
        state.isShouldGoBack = true
        updateState()
    }

    override fun delete(){
        state.isShowInter = true
        state.deleteState = DeleteState.Progress
        updateState()

        asynchronous {
            files.delete(state.selectedFiles.map { it.path })
            state.deleteState = DeleteState.Done
            updateState()
            suspendedUpdateFiles()
        }

    }

    override fun askDelete(){
        state.deleteState = DeleteState.Ask
        updateState()
    }

    override fun cancelDelete(){
        state.deleteState = DeleteState.Wait
        updateState()
    }

    override fun hideInter(){
        state.isShowInter = false
        updateState()
    }

    override fun completeDelete(){
        state.deleteState = DeleteState.Wait
        updateFiles()
    }

    override fun showSortingModeSelector(){
        state.isShowSortingModeSelector = true
        updateState()
    }

    override fun hideSortingModeSelector() {
        state.isShowSortingModeSelector = false
        updateState()
    }

    private fun updateState() {
        _stateHolder.update(state.copy())
    }
}


