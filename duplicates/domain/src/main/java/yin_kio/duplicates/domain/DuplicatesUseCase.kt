package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.State
import kotlin.coroutines.CoroutineContext

class DuplicatesUseCase(
    private val stateFlow: MutableStateFlow<State>,
    private val files: Files,
    private val imagesComparator: (ImageInfo, ImageInfo) -> Boolean,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) {

    private val state get() = stateFlow.value

    init {
        updateFiles()
    }

    fun updateFiles() {
        coroutineScope.launch(coroutineContext) {
            updateFilesSynchronously()
        }
    }

    private suspend fun updateFilesSynchronously() {
        stateFlow.value = state.copy(isInProgress = true)
        delay(1)
        stateFlow.value = state.copy(
            isInProgress = false,
            duplicatesList = getDuplicates()
        )
    }

    private suspend fun getDuplicates() = DuplicatesList(files.getImages().findDuplicates(imagesComparator))

}