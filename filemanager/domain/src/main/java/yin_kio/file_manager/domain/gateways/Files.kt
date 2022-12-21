package yin_kio.file_manager.domain.gateways

import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileMode

interface Files {
    suspend fun getFiles(fileMode: FileMode) : List<FileInfo>
    suspend fun delete(paths: List<String>)
}