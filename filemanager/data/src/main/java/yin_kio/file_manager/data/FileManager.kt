package yin_kio.file_manager.data

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

internal interface FileManager {

    suspend fun getAllFiles(folder: File) : List<File>
    suspend fun copyFiles(files: List<File>, destination: File)
    suspend fun moveFiles(files: List<File>, destination: File)
    suspend fun deleteFiles(files: List<File>) : Long

}

