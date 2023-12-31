package yin_kio_duplicates.data

import android.os.Environment
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.file_groups.FileGroups
import yin_kio.file_utils.FileUtils
import java.io.File

class FilesImpl(
    private val fileUtils: FileUtils
) : Files {

    override suspend fun getImages(): List<ImageInfo> {
        val fileGroups = FileGroups()
        return fileUtils.getAllFiles(Environment.getExternalStorageDirectory()).filter {
            fileGroups.images[it.extension.uppercase()] != null
        }.map { ImageInfo(it.absolutePath) }
    }

    override suspend fun delete(path: String) {
        fileUtils.deleteFile(path)
    }

    override suspend fun copy(path: String, destination: String) {
        fileUtils.copyFile(File(path), File(destination))
    }

    override fun folderForUnited(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_DCIM + "/"
    }
}