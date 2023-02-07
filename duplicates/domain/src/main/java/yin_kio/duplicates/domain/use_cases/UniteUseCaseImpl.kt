package yin_kio.duplicates.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.gateways.Ads
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.models.UniteWay
import kotlin.coroutines.CoroutineContext

internal class UniteUseCaseImpl(
    private val state: MutableStateHolder,
    private val ads: Ads,
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext,
    private val duplicatesRemover: DuplicateRemover
) : UniteUseCase{


    override fun unite(){
        if (!state.canUnite) return
        navigate(Destination.UniteProgress)
        async {
            state.apply {
                ads.preloadAd()

                uniteWay = selectUniteWay()
                val imagesForUniting = getImagesForUniting()
                if (imagesForUniting.isNotEmpty()) duplicatesRemover.invoke(imagesForUniting)
                delay(8000)

                navigate(Destination.Inter)
            }
        }
    }

    private fun MutableStateHolder.selectUniteWay() : UniteWay {
        return when (selected.isEmpty()) {
            true -> UniteWay.All
            false -> UniteWay.Selected
        }
    }

    private fun MutableStateHolder.getImagesForUniting(): List<Collection<ImageInfo>> {
        val forUniting = when (uniteWay) {
            UniteWay.Selected -> selected.map { it.value }.filter { it.size > 1 }
            UniteWay.All -> state.duplicatesLists.map { it.imageInfos }
        }
        return forUniting
    }

    private fun navigate(destination: Destination){
        state.destination = destination
        state.update()
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(coroutineContext) { action() }
    }
}