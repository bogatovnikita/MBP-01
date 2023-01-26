package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.OutBoundary

class UpdateUseCase(
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val mapper: DeleteFormMapper,
    private val deleteForm: DeleteForm,
    private val fileSystemInfoProvider: FileSystemInfoProvider,
    private val permissions: Permissions
) {

    fun update() = async {
        if (permissions.hasStoragePermission){
            outBoundary.outHasPermission(true)
            outBoundary.outUpdateProgress(true)
            outBoundary.outFileSystemInfo(getFileSystemInfo())
            outBoundary.outDeleteForm(getDeleteFormOut())
            outBoundary.outUpdateProgress(false)
        } else {
            outBoundary.outHasPermission(false)
        }

    }

    private fun getFileSystemInfo() : FileSystemInfo{
        return fileSystemInfoProvider.getFileSystemInfo()
    }

    private fun getDeleteFormOut() : DeleteFormOut{
        return mapper.createDeleteFormOut(deleteForm)
    }


    private fun async(action: suspend () -> Unit){
        coroutineScope.launch { action() }
    }

}