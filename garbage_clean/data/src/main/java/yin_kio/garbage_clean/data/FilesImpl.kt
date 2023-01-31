package yin_kio.garbage_clean.data

import android.os.Environment
import yin_kio.file_utils.FileUtilsImpl
import yin_kio.garbage_clean.domain.gateways.Files
import java.io.File

class FilesImpl : Files {

    private val fileUtils = FileUtilsImpl()

    override suspend fun delete(paths: List<String>) {
        fileUtils.deleteFiles(paths.map { File(it) })
    }

    override suspend fun getAll(): List<String> {
        return fileUtils.getAllFilesAndFolders(Environment.getExternalStorageDirectory()).map { it.absolutePath }
    }
}