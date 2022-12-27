package yin_kio.file_manager.presentation

import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_manager.domain.models.SortingMode

sealed interface Intention{

    object UpdateFiles : Intention
    class SwitchFileMode(val fileRequest: FileRequest) : Intention
    class SwitchSortingMode(val sortingMode: SortingMode) : Intention
    object SwitchSelectAll : Intention
    object SwitchShowingMode : Intention
    class SwitchSelectFile(val path: String) : Intention
    object GoBack : Intention
    object Delete : Intention
    object AskDelete : Intention
    object CancelDelete : Intention
    object CompleteDelete : Intention
    object HideInter : Intention
    object ShowSortingModeSelector : Intention

}