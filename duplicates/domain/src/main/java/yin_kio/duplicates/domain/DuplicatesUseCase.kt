package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import kotlin.coroutines.CoroutineContext

class DuplicatesUseCase(
    private val state: MutableStateHolder,
    private val files: Files,
    private val imagesComparator: (ImageInfo, ImageInfo) -> Boolean,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) {


    init {
        updateFiles()
    }

    fun updateFiles() {
        coroutineScope.launch(coroutineContext) {
            updateFilesSynchronously()
        }
    }

    private suspend fun updateFilesSynchronously() {
        state.isInProgress = true
        state.update()
        delay(1)

        state.isInProgress = false
        state.duplicatesList = getDuplicates()
        state.update()
    }

    private suspend fun getDuplicates() = files.getImages().findDuplicates(imagesComparator)


    fun switchGroupSelection(index: Int){
        val group = state.duplicatesList[index]
        if (state.selected[index] == null){
            state.selected[index] = group.toSet()
        } else {
            state.selected.remove(index)
        }

        state.update()
    }

}