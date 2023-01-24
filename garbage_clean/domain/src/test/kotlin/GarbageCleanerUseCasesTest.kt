import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.GarbageCleanerUseCases
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Files


@OptIn(ExperimentalCoroutinesApi::class)
class GarbageCleanerUseCasesTest {

    private val deleteForm: DeleteForm = mockk()
    private val files: Files = mockk()
    private lateinit var useCases: GarbageCleanerUseCases
    private lateinit var garbageFiles: GarbageFiles
    private lateinit var deleteRequest: DeleteRequest

    private val dispatcher = StandardTestDispatcher()
    private lateinit var coroutineScope: TestScope

    @BeforeEach
    fun setup() = runTest(dispatcher){
        coroutineScope = this
        garbageFiles = GarbageFiles()
        deleteRequest = DeleteRequest()
        useCases = GarbageCleanerUseCases(
            deleteForm = deleteForm,
            files = files,
            garbageFiles = garbageFiles,
            deleteRequest = deleteRequest,
            coroutineScope = coroutineScope,
            dispatcher = dispatcher
        )
    }



    @Test
    fun testSwitchSelectAll() = runTest{
        coEvery { deleteForm.switchSelectAll() } returns Unit

        useCases.switchSelectAll()

        coVerify { deleteForm.switchSelectAll() }
    }

    @Test
    fun testSwitchSelection() = runTest{
        coEvery { deleteForm.switchSelection(GarbageType.Apk) } returns Unit

        useCases.switchSelection(GarbageType.Apk)

        coVerify { deleteForm.switchSelection(GarbageType.Apk) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is not empty`() = runTest{
        deleteRequest.add(GarbageType.Apk)
        deleteRequest.add(GarbageType.Temp)

        garbageFiles[GarbageType.Apk] = mutableListOf(APK)
        garbageFiles[GarbageType.Temp] = mutableListOf(TEMP)


        coEvery { files.delete(listOf("apk", "temp")) } returns Unit

        useCases.startDeleteIfCan()

        coVerify { files.delete(listOf("apk", "temp")) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is empty`() = runTest{
        coEvery { files.delete(listOf()) } returns Unit

        useCases.startDeleteIfCan()

        coVerify(inverse = true) { files.delete(listOf()) }
    }

    private fun runTest(action: suspend TestScope.() -> Unit){
        coroutineScope.launch { action(this as TestScope) }
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}