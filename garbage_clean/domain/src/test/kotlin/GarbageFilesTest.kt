import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType

class GarbageFilesTest {

    @Test
    fun testPut(){
        val garbageFiles = GarbageFiles()

        garbageFiles[GarbageType.Apk] = listOf()

        assertTrue(garbageFiles[GarbageType.Apk] != null)
    }

}