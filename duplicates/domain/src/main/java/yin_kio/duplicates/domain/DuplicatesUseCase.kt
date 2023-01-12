package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import kotlin.coroutines.CoroutineContext

class DuplicatesUseCase(
    private val state: MutableStateHolder,
    private val files: Files,
    private val imagesComparator: ImagesComparator,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext
) {


    init {
        updateFiles()
    }

    fun updateFiles() = async {
        updateFilesSynchronously()
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
        if (selected[index] == null) {
            val set = mutableSetOf<ImageInfo>()
            set.addAll(duplicatesList[index])
            selected[index] = set
        } else {
            selected.remove(index)
        }


        update()
    }

    fun switchItemSelection(groupIndex: Int, path: String) = with(state){
        val item = duplicatesList[groupIndex].find { it.path == path }

        item?.also {
            val group = selected[groupIndex]
            when{
                group == null -> selected[groupIndex] = mutableSetOf(it)
                group.contains(it) -> group.remove(it)
                !group.contains(it) -> group.add(it)
            }

            if (group?.isEmpty() == true) selected.remove(groupIndex)
        }

        update()
    }

    fun isItemSelected(groupIndex: Int, path: String) : Boolean{
        return state.selected[groupIndex]?.contains(ImageInfo(path)) ?: false
    }

    fun navigate(destination: Destination){
        state.destination = destination
        state.update()
    }

    fun unite() = async {
        state.selected.forEach {
            val first = it.value.first()
            files.copy(first.path, files.unitedDestination())

            it.value.forEach {
                files.delete(it.path)
            }

        }
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(coroutineContext) { action() }
    }

}