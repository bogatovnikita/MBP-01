package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import kotlin.coroutines.CoroutineContext

internal class UpdateUseCase(
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val mapper: DeleteFormMapper,
    private val garbageFiles: GarbageFiles,
    private val files: Files,
    private val fileSystemInfoProvider: FileSystemInfoProvider,
    private val permissions: Permissions
) {

    fun update() = async {
        if (permissions.hasStoragePermission){
            outBoundary.outDeleteProgress(DeleteProgressState.Wait)
            outBoundary.outHasPermission(true)
            outBoundary.outUpdateProgress(true)
            outBoundary.outFileSystemInfo(getFileSystemInfo())

            garbageFiles.setFiles(files.getAll())
            if (garbageFiles.isNotEmpty()) garbageFiles.deleteForm.switchSelectAll()

            outBoundary.outDeleteForm(getDeleteFormOut())
            outBoundary.outUpdateProgress(false)
        } else {
            outBoundary.outHasPermission(false)
        }

    }

    private suspend fun getFileSystemInfo() : FileSystemInfo{
        return fileSystemInfoProvider.getFileSystemInfo()
    }

    private fun getDeleteFormOut() : DeleteFormOut{
        return mapper.createDeleteFormOut(garbageFiles.deleteForm)
    }


    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

}