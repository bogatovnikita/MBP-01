package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.State
import kotlin.coroutines.CoroutineContext

class DuplicatesUseCase(
    private val state: State,
    private val files: Files,
    private val imagesComparator: (ImageInfo, ImageInfo) -> Boolean,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) {

    init {
        updateFiles()

    }

    private fun updateFiles() {
        coroutineScope.launch(coroutineContext) {
            state.isInProgress = true
            val images = files.getImages()

            state.duplicatesList = DuplicatesList(images.findDuplicates(imagesComparator))
            state.isInProgress = false
        }
    }

}