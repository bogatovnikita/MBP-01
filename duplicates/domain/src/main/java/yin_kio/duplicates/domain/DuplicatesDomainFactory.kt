package yin_kio.duplicates.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.duplicates.domain.gateways.Ads
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.models.StateHolder
import yin_kio.duplicates.domain.use_cases.DuplicateRemoverImpl
import yin_kio.duplicates.domain.use_cases.DuplicateUseCases
import yin_kio.duplicates.domain.use_cases.DuplicatesUseCasesImpl
import yin_kio.duplicates.domain.use_cases.UniteUseCaseImpl

object DuplicatesDomainFactory {

    fun createUseCaseAndStateHolder(
        coroutineScope: CoroutineScope,
        files: Files,
        imagesComparator: ImagesComparator,
        permissions: Permissions,
        ads: Ads
    ) : Pair<DuplicateUseCases, StateHolder>{

        val stateHolder = MutableStateHolder(
            coroutineScope = coroutineScope,
            coroutineContext = Dispatchers.Default
        )

        val duplicateRemover = DuplicateRemoverImpl(files)

        val useCase = DuplicatesUseCasesImpl(
            state = stateHolder,
            files = files,
            imagesComparator = imagesComparator,
            permissions = permissions,
            coroutineScope = coroutineScope,
            coroutineContext = Dispatchers.IO,
            uniteUseCase = UniteUseCaseImpl(
                state = stateHolder,
                ads = ads,
                coroutineScope = coroutineScope,
                coroutineContext = Dispatchers.IO,
                duplicatesRemover = duplicateRemover
            )
        )
        return Pair(useCase, stateHolder)
    }



}