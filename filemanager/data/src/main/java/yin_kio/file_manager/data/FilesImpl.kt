package yin_kio.file_manager.data

import yin_kio.file_manager.domain.entities.FileExtensionGroups
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.models.FileGroup
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_utils.FileUtils
import java.io.File

internal class FilesImpl(
    private val fileManager: FileUtils,
    private val folders: Folders
) : Files {

    private val groups = FileExtensionGroups()

    override suspend fun getFiles(fileRequest: FileRequest): List<FileInfo> {
        val allFiles = fileManager.getAllFiles(folders.root)

        return when(fileRequest){
            FileRequest.AllFiles -> allFiles
            FileRequest.Images -> allFiles.filter { isImage(it.extension) }
            FileRequest.Video -> allFiles.filter { isVideo(it.extension) }
            FileRequest.Documents -> allFiles.filter { isDocument(it.extension) }
            FileRequest.Audio -> allFiles.filter { isAudio(it.extension) }
        }.toFileInfoList()
    }

    private fun List<File>.toFileInfoList(): List<FileInfo> =
        map { fileInfo(it) }

    private fun fileInfo(it: File) = FileInfo(
        fileGroup = fileGroup(it.extension),
        name = it.name,
        time = it.lastModified(),
        size = it.length(),
        path = it.absolutePath,
    )

    private fun fileGroup(extension: String?) : FileGroup{
        return when{
            extension == null -> FileGroup.Unknown
            isImage(extension) -> FileGroup.Images
            isVideo(extension) -> FileGroup.Video
            isDocument(extension) -> FileGroup.Documents
            isAudio(extension) -> FileGroup.Audio
            else -> FileGroup.Unknown
        }
    }

    private fun isAudio(extension: String) = groups.audio[extension.uppercase()] != null

    private fun isDocument(extension: String) = groups.documents[extension.uppercase()] != null

    private fun isVideo(extension: String) = groups.video[extension.uppercase()] != null

    private fun isImage(extension: String) = groups.images[extension.uppercase()] != null


    override suspend fun delete(paths: List<String>) {
        val files = paths.map { File(it) }
        fileManager.deleteFiles(files)
    }
}