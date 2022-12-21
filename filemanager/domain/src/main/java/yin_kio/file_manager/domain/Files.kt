package yin_kio.file_manager.domain

interface Files {
    suspend fun getFiles(fileMode: FileMode) : List<FileInfo>
    suspend fun delete(paths: List<String>)
}