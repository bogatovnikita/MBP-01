package yin_kio.garbage_clean.domain.entities

import java.io.File

class GarbageFileGrouper {

    private val apks = arrayOf(APK)
    private val temp = arrayOf(TMP, TEMP, TEMP_PATH, TMP_PATH)
    private val rest = arrayOf(DAT, LOG, LOG_PATH)
    private val thumb = arrayOf(THUMB, THUMB_PATH, THUMBNAILS_PATH)

    fun group(files: List<String>) : GarbageFiles{
        val garbageFiles = GarbageFiles()

        files.forEach {

            val file = File(it)
            if(file.isDirectory && file.list().isNullOrEmpty()) garbageFiles.addTo(GarbageType.EmptyFolders, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(apks)) garbageFiles.addTo(GarbageType.Apk, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(temp)) garbageFiles.addTo(GarbageType.Temp, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(rest)) garbageFiles.addTo(GarbageType.RestFiles, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(thumb)) garbageFiles.addTo(GarbageType.Thumbnails, file.absolutePath)

        }

        return garbageFiles
    }

    private fun String.containsMask(masks: Array<String>) : Boolean{
        masks.forEach {
            if (contains(it)) return true
        }
        return false
    }

    companion object{
        const val APK = ".apk"
        const val TMP = ".tmp"
        const val TEMP = ".temp"
        const val TEMP_PATH = "/temp/"
        const val TMP_PATH = "/tmp/"
        const val DAT = ".dat"
        const val LOG = ".log"
        const val LOG_PATH = "/log/"
        const val THUMB = ".thumb"
        const val THUMB_PATH = "/thumb/"
        const val THUMBNAILS_PATH = "/thumbnails/"
    }


}