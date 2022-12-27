package yin_kio.file_manager.data

import yin_kio.file_manager.domain.entities.FileExtensionGroups
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.models.FileGroup
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileRequest
import java.io.File

internal class FilesImpl(
    private val fileManager: FileManager,
    private val folders: Folders
) : Files {

    private val groups = FileExtensionGroups()

    override suspend fun getFiles(fileRequest: FileRequest): List<FileInfo> {
        val allFiles = fileManager.getAllFiles(folders.root)

        return when(fileRequest){
            FileRequest.AllFiles -> allFiles
            FileRequest.Images -> allFiles.filter { groups.images[it.extension.uppercase()] != null }
            FileRequest.Video -> allFiles.filter { groups.video[it.extension.uppercase()] != null }
            FileRequest.Documents -> allFiles.filter { groups.documents[it.extension.uppercase()] != null }
            FileRequest.Audio -> allFiles.filter { groups.audio[it.extension.uppercase()] != null }
        }.toFileInfoList()
    }

    private fun List<File>.toFileInfoList(): List<FileInfo> =
        map { fileInfo(it) }

    private fun fileInfo(it: File) = FileInfo(
        fileGroup = FileGroup.Images,
        name = it.name,
        time = it.lastModified(),
        size = it.length(),
        path = it.absolutePath,
    )


    override suspend fun delete(paths: List<String>) {
        val files = paths.map { File(it) }
        fileManager.deleteFiles(files)
    }
}