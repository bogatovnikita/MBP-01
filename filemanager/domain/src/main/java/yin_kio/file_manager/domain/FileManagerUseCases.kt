package yin_kio.file_manager.domain

import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_manager.domain.models.SortingMode
import yin_kio.file_manager.domain.models.StateHolder

interface FileManagerUseCases {
    val stateHolder: StateHolder

    fun updateFiles()

    fun switchFileMode(fileRequest: FileRequest)
    fun switchSortingMode(sortingMode: SortingMode)
    fun switchSelectAll()
    fun switchShowingMode()
    fun switchSelectFile(path: String)

    fun goBack()

    fun delete()
    fun askDelete()
    fun cancelDelete()
    fun completeDelete()

    fun hideInter()

    fun showSortingModeSelector()
    fun hideSortingModeSelector()

    fun close()

}