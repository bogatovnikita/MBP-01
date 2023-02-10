package yin_kio.file_manager.presentation

import kotlinx.coroutines.flow.map
import yin_kio.file_manager.domain.FileManagerUseCases
import yin_kio.file_manager.presentation.models.ScreenState
import yin_kio.file_manager.presentation.presenters.FileManagerPresenter

class FileManagerViewModel(
    private val fileManagerUseCases: FileManagerUseCases,
    private val presenter: FileManagerPresenter
) {

    val flow = fileManagerUseCases.stateHolder.flow
        .map {
            ScreenState(
                isClosed = it.isClosed,
                fileRequest = it.fileRequest,
                isAllSelected = it.isAllSelected,
                listShowingMode = it.listShowingMode,
                sortingIconColor = presenter.presentSortingMode(it.sortingMode),
                deleteButtonBg = presenter.presentMainButton(it.canDelete),
                hasPermission = it.hasPermission,
                files = it.files,
                listShowingModeIconRes = presenter.presentListShowingModeIcon(it.listShowingMode),
                isShowSortingModeSelector = it.isShowSortingModeSelector,
                sortingMode = it.sortingMode,
                inProgress = it.inProgress,
                progressAlpha = presenter.presentProgressAlpha(it.inProgress),
                deleteState = it.deleteState,
                askDeleteTitle = presenter.presentAskDeleteTitle(it.fileRequest),
                deleteProgressTitle = presenter.presentDeleteProgressTitle(it.fileRequest),
                isShowInter = it.isShowInter,
                doneTitle = presenter.presentDoneDialogTitle(it.fileRequest)
            )
        }


    fun obtainIntention(intention: Intention){
        when(intention){
            Intention.AskDelete -> fileManagerUseCases.askDelete()
            Intention.CancelDelete -> fileManagerUseCases.cancelDelete()
            Intention.CompleteDelete -> fileManagerUseCases.completeDelete()
            Intention.Delete -> fileManagerUseCases.delete()
            Intention.GoBack -> fileManagerUseCases.goBack()
            Intention.HideInter -> fileManagerUseCases.hideInter()
            Intention.ShowSortingModeSelector -> fileManagerUseCases.showSortingModeSelector()
            is Intention.SwitchFileMode -> fileManagerUseCases.switchFileMode(intention.fileRequest)
            Intention.SwitchSelectAll -> fileManagerUseCases.switchSelectAll()
            is Intention.SwitchSelectFile -> fileManagerUseCases.switchSelectFile(intention.path)
            Intention.SwitchShowingMode -> fileManagerUseCases.switchShowingMode()
            is Intention.SwitchSortingMode -> fileManagerUseCases.switchSortingMode(intention.sortingMode)
            Intention.UpdateFiles -> fileManagerUseCases.updateFiles()
            Intention.HideSortingModeSelector -> fileManagerUseCases.hideSortingModeSelector()
            Intention.Close -> fileManagerUseCases.close()
        }
    }


}