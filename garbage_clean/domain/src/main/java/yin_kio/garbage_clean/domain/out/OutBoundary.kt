package yin_kio.garbage_clean.domain.out

import yin_kio.garbage_clean.domain.entities.FileSystemInfo

interface OutBoundary {

    fun outUpdateProgress(isInProgress: Boolean)
    fun outDeleteForm(deleteFormOut: DeleteFormOut)
    fun outFileSystemInfo(fileSystemInfo: FileSystemInfo)
    fun outHasPermission(has: Boolean)
    fun outDeleteProgress(isInProgress: Boolean)

}