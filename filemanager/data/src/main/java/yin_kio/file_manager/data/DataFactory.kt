package yin_kio.file_manager.data

import android.content.Context
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_utils.FileUtilsImpl

object DataFactory {

    fun createPermissionChecker(context: Context) : PermissionChecker{
        return PermissionCheckerImpl(context)
    }

    fun createFiles() : Files{
        return FilesImpl(
            FileUtilsImpl(),
            AndroidFolders()
        )
    }

}