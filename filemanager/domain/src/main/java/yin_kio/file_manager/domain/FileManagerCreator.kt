package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.file_manager.domain.gateways.Ads
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_manager.domain.models.MutableStateHolder

object FileManagerCreator {

    fun create(
        permissionChecker: PermissionChecker,
        files: Files,
        coroutineScope: CoroutineScope,
        ads: Ads
    ) : FileManagerUseCases{
        return FileManagerUseCasesImpl(
            _stateHolder = MutableStateHolder(),
            permissionChecker = permissionChecker,
            files = files,
            coroutineScope = coroutineScope,
            coroutineContext = Dispatchers.IO,
            ads = ads
        )
    }

}