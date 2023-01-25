import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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


    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {
            garbageFiles = GarbageFiles()
            deleteRequest = DeleteRequest()
            useCases = GarbageCleanerUseCases(
                deleteForm = deleteForm,
                files = files,
                garbageFiles = garbageFiles,
                deleteRequest = deleteRequest,
                coroutineScope = this,
                dispatcher = coroutineContext
            )
            testBody()
        }
    }


    @Test
    fun testSwitchSelectAll() = setupTest {

        coEvery { deleteForm.switchSelectAll() } returns Unit

        useCases.switchSelectAll()
        wait()

        coVerify { deleteForm.switchSelectAll() }
    }


    @Test
    fun testSwitchSelection() = setupTest{
        coEvery { deleteForm.switchSelection(GarbageType.Apk) } returns Unit

        useCases.switchSelection(GarbageType.Apk)
        wait()

        coVerify { deleteForm.switchSelection(GarbageType.Apk) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is not empty`() = setupTest{
        deleteRequest.add(GarbageType.Apk)
        deleteRequest.add(GarbageType.Temp)

        garbageFiles[GarbageType.Apk] = mutableListOf(APK)
        garbageFiles[GarbageType.Temp] = mutableListOf(TEMP)


        coEvery { files.delete(listOf(APK, TEMP)) } returns Unit

        useCases.startDeleteIfCan()
        wait()

        coVerify { files.delete(listOf(APK, TEMP)) }
    }

    @Test
    fun `testStartDeleteIfCan deleteRequest is empty`() = setupTest{
        coEvery { files.delete(listOf()) } returns Unit

        useCases.startDeleteIfCan()

        coVerify(inverse = true) { files.delete(listOf()) }
    }



    private fun TestScope.wait() {
        advanceUntilIdle()
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}