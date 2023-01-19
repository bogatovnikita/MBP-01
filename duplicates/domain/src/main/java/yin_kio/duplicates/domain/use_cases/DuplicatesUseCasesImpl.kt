package yin_kio.duplicates.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.findDuplicates
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.*
import kotlin.coroutines.CoroutineContext

internal class DuplicatesUseCasesImpl(
    private val state: MutableStateHolder,
    private val files: Files,
    private val imagesComparator: ImagesComparator,
    private val permissions: Permissions,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext,
    private val uniteUseCase: UniteUseCase
) : DuplicateUseCases{


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

        duplicatesLists = getDuplicates().mapIndexed { index, imageInfos -> DuplicatesList(index, imageInfos)  }
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
            set.addAll(duplicatesLists[index].data)
            selected[index] = set
        } else {
            selected.remove(index)
        }

        update()
    }


    override fun switchItemSelection(groupIndex: Int, path: String) = with(state){
        val item = duplicatesLists[groupIndex].data.find { it.path == path }

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

    override fun navigate(destination: Destination){
        state.destination = destination
        state.update()
    }

    override fun unite(){
        uniteUseCase.unite()
    }




    override fun closeInter(){
        state.destination = when(state.uniteWay){
            UniteWay.Selected -> Destination.AskContinue
            UniteWay.All -> Destination.AdvicesWithDialog
        }
        state.update()
    }

    override fun continueUniting(){
        state.destination = Destination.List
        updateFiles()
        state.update()
    }

    override fun completeUniting(){
        state.destination = Destination.Advices
        state.update()
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(coroutineContext) { action() }
    }

}