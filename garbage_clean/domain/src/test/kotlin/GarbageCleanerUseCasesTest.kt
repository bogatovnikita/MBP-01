import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.GarbageCleanerUseCases
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Files

class GarbageCleanerUseCasesTest {

    private val deleteForm: DeleteForm = mockk()
    private val files: Files = mockk()
    private lateinit var useCases: GarbageCleanerUseCases
    private lateinit var garbageFiles: GarbageFiles
    private lateinit var deleteRequest: DeleteRequest

    @BeforeEach
    fun setup(){
        garbageFiles = GarbageFiles()
        deleteRequest = DeleteRequest()
        useCases = GarbageCleanerUseCases(deleteForm, files, garbageFiles, deleteRequest)
    }

    @Test
    fun testSelectAll(){
        coEvery { deleteForm.selectAll() } returns Unit

        useCases.selectAll()

        coVerify { deleteForm.selectAll() }
    }

    @Test
    fun testSwitchSelection(){
        coEvery { deleteForm.switchSelection(GarbageType.Apk) } returns Unit

        useCases.switchSelection(GarbageType.Apk)

        coVerify { deleteForm.switchSelection(GarbageType.Apk) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is not empty`(){
        deleteRequest.add(GarbageType.Apk)
        deleteRequest.add(GarbageType.Temp)

        garbageFiles[GarbageType.Apk] = mutableListOf(APK)
        garbageFiles[GarbageType.Temp] = mutableListOf(TEMP)


        coEvery { files.delete(listOf("apk", "temp")) } returns Unit

        useCases.startDeleteIfCan()

        coVerify { files.delete(listOf("apk", "temp")) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is empty`(){
        coEvery { files.delete(listOf()) } returns Unit

        useCases.startDeleteIfCan()

        coVerify(inverse = true) { files.delete(listOf()) }
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}