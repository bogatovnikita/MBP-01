package yin_kio.file_manager.domain.gateways

import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileRequest

interface Files {
    suspend fun getFiles(fileRequest: FileRequest) : List<FileInfo>
    suspend fun delete(paths: List<String>)
}