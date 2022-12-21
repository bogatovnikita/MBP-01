package yin_kio.file_manager.domain

interface Files {
    suspend fun getFiles(fileMode: FileMode) : List<FileInfo>
}