package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import kotlin.coroutines.CoroutineContext

class DuplicatesUseCase(
    private val state: MutableStateHolder,
    private val files: Files,
    private val imagesComparator: ImagesComparator,
    private val permissions: Permissions,
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
        if (hasNotPermission()) return@with


        isInProgress = true
        update()
        delay(1)

        isInProgress = false
        duplicatesList = getDuplicates()

        update()
    }

    private fun hasNotPermission(): Boolean {
        val hasPermission = permissions.hasStoragePermissions
        if (!hasPermission) {
            navigate(Destination.Permission)
            return true
        }
        return false
    }


    private suspend fun getDuplicates() = files.getImages().findDuplicates(imagesComparator)


    fun selectGroup(index: Int) = with(state){
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
        println("navigate $destination")
        state.destination = destination
        state.update()
    }

    fun uniteSelected(){
        navigate(Destination.UniteProgress)
        async {
            state.selected.forEach {
                val first = it.value.first()
                files.copy(first.path, files.folderForUnited())

                it.value.forEach {
                    files.delete(it.path)
                }
            }
            navigate(Destination.Inter)
        }
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(coroutineContext) { action() }
    }

}