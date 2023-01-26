import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.GarbageCleanerUseCases
import yin_kio.garbage_clean.domain.UpdateUseCase
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.OutBoundary


@OptIn(ExperimentalCoroutinesApi::class)
class GarbageCleanerUseCasesTest {

    private val files: Files = mockk()
    private val outBoundary: OutBoundary = mockk()
    private val mapper: DeleteFormMapper = mockk()
    private val updateUseCase: UpdateUseCase = mockk()
    private val ads: Ads = mockk()
    private lateinit var useCases: GarbageCleanerUseCases
    private val garbageFiles: GarbageFiles = spyk()
    private lateinit var deleteRequest: DeleteRequest

    private val deleteFormOut = DeleteFormOut()


    init {
        coEvery { mapper.createDeleteFormOut(garbageFiles.deleteForm) } returns deleteFormOut
        coEvery {
            files.delete(listOf(APK, TEMP))
            files.delete(listOf())

            garbageFiles.deleteForm.switchSelectAll()
            garbageFiles.deleteForm.switchSelection(GarbageType.Apk)

            outBoundary.outDeleteProgress(true)
            outBoundary.outDeleteProgress(false)
            outBoundary.outDeleteForm(deleteFormOut)

            updateUseCase.update()

            ads.preloadAd()
        } returns Unit
    }

    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {
            deleteRequest = DeleteRequest()
            useCases = GarbageCleanerUseCases(
                files = files,
                garbageFiles = garbageFiles,
                deleteRequest = deleteRequest,
                coroutineScope = this,
                outBoundary = outBoundary,
                mapper = mapper,
                updateUseCase = updateUseCase,
                ads = ads
            )
            testBody()
        }
    }


    @Test
    fun testSwitchSelectAll() = setupTest {
        useCases.switchSelectAll()
        wait()

        coVerify {
            garbageFiles.deleteForm.switchSelectAll()
            outBoundary.outDeleteForm(deleteFormOut)
        }
    }


    @Test
    fun testSwitchSelection() = setupTest{
        useCases.switchSelection(GarbageType.Apk)
        wait()

        coVerify { garbageFiles.deleteForm.switchSelection(GarbageType.Apk) }
    }

    @Test
    fun `test deleteIfCan deleteRequest is not empty`() = setupTest{
        deleteRequest.add(GarbageType.Apk)
        deleteRequest.add(GarbageType.Temp)

        garbageFiles[GarbageType.Apk] = mutableSetOf(APK)
        garbageFiles[GarbageType.Temp] = mutableSetOf(TEMP)

        useCases.deleteIfCan()
        wait()

        coVerifyOrder {
            ads.preloadAd()
            outBoundary.outDeleteProgress(true)
            files.delete(listOf(APK, TEMP))
            outBoundary.outDeleteProgress(false)
        }
    }


    @Test
    fun `test deleteIfCan deleteRequest is empty`() = setupTest{
        useCases.deleteIfCan()

        coVerify(inverse = true) { files.delete(listOf()) }
    }

    @Test
    fun testUpdate() = setupTest{
        useCases.update()

        coVerify { updateUseCase.update() }
    }



    private fun TestScope.wait() {
        advanceUntilIdle()
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}