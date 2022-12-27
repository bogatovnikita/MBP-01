package yin_kio.file_manager.data

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

internal class FileManagerImpl : FileManager {

    override suspend fun getAllFiles(folder: File): List<File> {
        fun getFiles(files: Array<File>) : List<File> {
            val tempFiles = mutableListOf<File>()

            files.forEach { file ->
                if (file.isDirectory){
                    file.listFiles()?.let {
                        tempFiles.addAll(getFiles(it))
                    }
                } else {
                    tempFiles.add(file)
                }
            }
            return tempFiles
        }

        if (!folder.exists()) return listOf()
        val files = folder.listFiles() ?: return listOf()

        return getFiles(files)
    }

    override suspend fun copyFiles(files: List<File>, destination: File) {
        if (destination.isFile) throw IOException("Destination is not folder: ${destination.absolutePath}")
        files.forEach { copyFile(destination, it) }
    }



    override suspend fun moveFiles(files: List<File>, destination: File) {
        copyFiles(files, destination)
        deleteFiles(files)
    }

    private fun copyFile(destination: File, file: File) {
        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null

        try {
            outputChannel =
                FileOutputStream(destination.absolutePath + "/${file.name}").channel
            inputChannel = FileInputStream(file).channel

            outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
        } finally {
            inputChannel?.close()
            outputChannel?.close()
        }
    }

    override suspend fun deleteFiles(files: List<File>) : Long {
        var size = 0L
        files.forEach { runCatching {
                if (it.delete()){
                    size += it.length()
                }
            }
        }
        return size
    }
}