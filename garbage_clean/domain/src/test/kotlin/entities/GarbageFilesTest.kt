package entities

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType

class GarbageFilesTest {

    private lateinit var garbageFiles: GarbageFiles

    @BeforeEach
    fun setup(){
        garbageFiles = GarbageFiles()
    }

    @Test
    fun testPut(){
        garbageFiles[GarbageType.Apk] = mutableListOf()

        assertTrue(garbageFiles[GarbageType.Apk] != null)
    }

    @Test
    fun testAddTo(){
        val path = "/path/to/file.apk"
        garbageFiles.addTo(GarbageType.Apk, path)

        val path2 = "/path/to/file2.apk"
        garbageFiles.addTo(GarbageType.Apk, path2)

        assertTrue(garbageFiles[GarbageType.Apk]!!.contains(path))
        assertTrue(garbageFiles[GarbageType.Apk]!!.contains(path2))
    }

}