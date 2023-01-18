package yin_kio.duplicates.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.findDuplicates
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.models.UniteWay
import kotlin.coroutines.CoroutineContext

internal class DuplicatesUseCaseImpl(
    private val state: MutableStateHolder,
    private val files: Files,
    private val imagesComparator: ImagesComparator,
    private val permissions: Permissions,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext,
    private val duplicateRemover: DuplicateRemover
) : DuplicateUseCase{


    init {
        updateFiles()
    }

    override fun updateFiles() = async {
        updateFilesSynchronously()
    }

    private suspend fun updateFilesSynchronously() = with(state) {
        if (hasNotPermission()) return@with


        isInProgress = true
        delay(1)
        update()

        duplicatesList = getDuplicates()
        isInProgress = false

        update()
    }

    private fun hasNotPermission(): Boolean {
        val hasPermission = permissions.hasStoragePermissions
        if (!hasPermission) {
            navigate(Destination.Permission)
            return true
        }

        navigate(Destination.List)
        return false
    }


    private suspend fun getDuplicates() = files.getImages().findDuplicates(imagesComparator)


    override fun switchGroupSelection(index: Int) = with(state){
        if (selected[index] == null) {
            val set = mutableSetOf<ImageInfo>()
            set.addAll(duplicatesList[index])
            selected[index] = set
        } else {
            selected.remove(index)
        }

        update()
    }


    override fun switchItemSelection(groupIndex: Int, path: String) = with(state){
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

    private fun MutableStateHolder.selectUniteWay() : UniteWay {
        return when (selected.isEmpty()) {
            true -> UniteWay.All
            false -> UniteWay.Selected
        }
    }

    override fun navigate(destination: Destination){
        state.destination = destination
        state.update()
    }

    override fun unite(){
        navigate(Destination.UniteProgress)
        async {
            state.apply {
                uniteWay = selectUniteWay()
                val imagesForUniting = getImagesForUniting()
                if (imagesForUniting.isNotEmpty()) duplicateRemover.invoke(imagesForUniting)
                navigate(Destination.Inter)
            }
        }
    }

    private fun MutableStateHolder.getImagesForUniting(): List<Collection<ImageInfo>> {
        val forUniting = when (uniteWay) {
            UniteWay.Selected -> selected.map { it.value }.filter { it.size > 1 }
            UniteWay.All -> state.duplicatesList
        }
        return forUniting
    }


    override fun closeInter(){
        state.destination = when(state.uniteWay){
            UniteWay.Selected -> Destination.DoneSelected
            UniteWay.All -> Destination.DoneAll
        }
    }

    override fun continueUniting(){
        state.destination = Destination.List
    }

    override fun completeUniting(){
        state.destination = Destination.DoneAll
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(coroutineContext) { action() }
    }

}