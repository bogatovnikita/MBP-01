package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanUseCases
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanerUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase

object GarbageCleanFactory {

    fun createUseCases(
        files: Files,
        outBoundary: OutBoundary,
        coroutineScope: CoroutineScope,
        fileSystemInfoProvider: FileSystemInfoProvider,
        permissions: Permissions,
        ads: Ads
    ) : GarbageCleanUseCases{
        val garbageFiles = GarbageFiles()
        val mapper = DeleteFormMapper()

        return GarbageCleanerUseCasesImpl(
            garbageFiles = garbageFiles,
            mapper = mapper,
            files = files,
            outBoundary = outBoundary,
            coroutineScope = coroutineScope,
            UpdateUseCase(
                outBoundary = outBoundary,
                coroutineScope = coroutineScope,
                mapper = mapper,
                garbageFiles = garbageFiles,
                files = files,
                fileSystemInfoProvider = fileSystemInfoProvider,
                permissions = permissions
            ),
            ads = ads
        )
    }

}