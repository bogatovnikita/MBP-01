package entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.DeleteRequestInterpreter
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType

class DeleteRequestInterpreterTest {

    @Test
    fun testInterpret(){
        val interpreter = DeleteRequestInterpreter(garbageFiles())

        val deleteRequest = DeleteRequest()
        deleteRequest.add(GarbageType.Apk)
        deleteRequest.add(GarbageType.Temp)

        assertEquals(listOf(APK, TEMP), interpreter.interpret(deleteRequest))
    }

    private fun garbageFiles(): GarbageFiles {
        val garbageFiles = GarbageFiles()

        garbageFiles[GarbageType.Apk] = mutableListOf(APK)
        garbageFiles[GarbageType.Temp] = mutableListOf(TEMP)
        return garbageFiles
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}