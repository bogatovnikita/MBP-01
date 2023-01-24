package entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageFileGrouper
import yin_kio.garbage_clean.domain.entities.GarbageType
import java.io.File

class GarbageFilesGrouperTest {

    private val grouper = GarbageFileGrouper()
    private val apk = listOf(
        "app.apk",
    )
    private val temp = listOf(
        "temp_file.temp",
        "temp_file.tmp",
        "/temp/temp_file",
        "/tmp/temp_file",
    )

    private val rest = listOf(
        "log.log",
        "/log/logfile",
        "dat.dat",
    )

    private val thumb = listOf(
        "thumb.thumb",
        "/thumb/thumbnail",
        "/thumbnails/thumbnail"
    )

    private val other = listOf(
        "some other file 1",
        "some other file 2",
        "some other file 3",
        "temporary_need_file"
    )

    private val emptyFolders = listOf("src/test/resources/empty_folder")

    @Test
    fun group(){
        val input = apk + temp + rest + thumb + emptyFolders + other

        val garbageFiles = grouper.group(input)

        assertEquals(garbageFiles[GarbageType.Apk]!!, apk.map { File(it).absolutePath })
        assertEquals(garbageFiles[GarbageType.Temp]!!, temp.map { File(it).absolutePath })
        assertEquals(garbageFiles[GarbageType.RestFiles]!!, rest.map { File(it).absolutePath })
        assertEquals(garbageFiles[GarbageType.Thumbnails]!!, thumb.map { File(it).absolutePath })
        assertEquals(garbageFiles[GarbageType.EmptyFolders]!!, emptyFolders.map { File(it).absolutePath })
    }

}