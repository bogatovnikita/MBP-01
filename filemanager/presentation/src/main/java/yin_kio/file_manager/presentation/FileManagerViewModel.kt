package yin_kio.file_manager.presentation

import kotlinx.coroutines.flow.map
import yin_kio.file_manager.domain.FileManager
import yin_kio.file_manager.domain.models.StateHolder
import yin_kio.file_manager.presentation.models.UiState

class FileManagerViewModel(
    private val fileManager: FileManager,
    private val presenter: Presenter
) {

    val flow = fileManager.stateHolder.flow
        .map {
            UiState(
                fileRequest = it.fileRequest,
                isAllSelected = it.isAllSelected,
                layoutManager = presenter.presentShowingMode(it.listShowingMode),
                sortingIconColor = presenter.presentSortingMode(it.sortingMode),
                deleteButtonColor = presenter.presentMainButton(it.canDelete),
                hasPermission = it.hasPermission
            )
        }


    fun obtainIntention(intention: Intention){
        when(intention){
            Intention.AskDelete -> fileManager.askDelete()
            Intention.CancelDelete -> fileManager.cancelDelete()
            Intention.CompleteDelete -> fileManager.completeDelete()
            Intention.Delete -> fileManager.delete()
            Intention.GoBack -> fileManager.goBack()
            Intention.HideInter -> fileManager.hideInter()
            Intention.ShowSortingModeSelector -> fileManager.showSortingModeSelector()
            is Intention.SwitchFileMode -> fileManager.switchFileMode(intention.fileRequest)
            Intention.SwitchSelectAll -> fileManager.switchSelectAll()
            is Intention.SwitchSelectFile -> fileManager.switchSelectFile(intention.path)
            Intention.SwitchShowingMode -> fileManager.switchShowingMode()
            is Intention.SwitchSortingMode -> fileManager.switchSortingMode(intention.sortingMode)
            Intention.UpdateFiles -> fileManager.updateFiles()
        }
    }


}