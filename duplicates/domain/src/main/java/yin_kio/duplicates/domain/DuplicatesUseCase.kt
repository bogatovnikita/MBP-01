package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.gateways.Files
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

    private suspend fun updateFilesSynchronously() = with(state) {
        isInProgress = true
        update()
        delay(1)

        isInProgress = false
        duplicatesList = getDuplicates()

        update()
    }


    private suspend fun getDuplicates() = files.getImages().findDuplicates(imagesComparator)


    fun switchGroupSelection(index: Int) = with(state){
        val group = duplicatesList[index].toSet()

        if (!selected.containsAll(group)){
            selected.addAll(group)
        } else {
            selected.removeAll(group)
        }

        update()
    }

    fun switchItemSelection(groupIndex: Int, path: String) = with(state){
        val item = duplicatesList[groupIndex].find { it.path == path }

        item?.let {
            if (selected.contains(item)){
                selected.remove(item)
            } else {
                selected.add(item)
            }
        }

        update()
    }

    fun isItemSelected(path: String) : Boolean{
        return state.selected.contains(ImageInfo(path))
    }

}