import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.UpdateUseCase
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.OutBoundary


@OptIn(ExperimentalCoroutinesApi::class)
class UpdateUseCaseTest {

    private val outBoundary: OutBoundary = mockk()
    private val fileSystemInfoProvider: FileSystemInfoProvider = mockk()
    private val permissions: Permissions = mockk()
    private lateinit var updateUseCase: UpdateUseCase

    private val fileSystemInfo = FileSystemInfo()
    private val deleteFormOut = DeleteFormOut()

    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {
            coEvery {
                outBoundary.outUpdateProgress(true)
                outBoundary.outFileSystemInfo(fileSystemInfo)
                outBoundary.outDeleteForm(deleteFormOut)
                outBoundary.outUpdateProgress(false)
                outBoundary.outHasPermission(false)
                outBoundary.outHasPermission(true)
            } returns Unit

            updateUseCase = UpdateUseCase(
                outBoundary = outBoundary,
                coroutineScope = this,
                mapper = DeleteFormMapper(),
                deleteForm = DeleteForm(),
                fileSystemInfoProvider = fileSystemInfoProvider,
                permissions = permissions
            )
            testBody()
        }
    }

    @Test
    fun `testUpdate with has permission`() = setupTest{

        coEvery { permissions.hasStoragePermission } returns true
        coEvery { fileSystemInfoProvider.getFileSystemInfo() } returns FileSystemInfo()

        updateUseCase.update()
        wait()

        assertUpdateOrder(fileSystemInfo, deleteFormOut)
    }

    private fun assertUpdateOrder(
        fileSystemInfo: FileSystemInfo,
        deleteFormOut: DeleteFormOut
    ) {
        coVerifyOrder {
            outBoundary.outHasPermission(true)
            outBoundary.outUpdateProgress(true)
            outBoundary.outFileSystemInfo(fileSystemInfo)
            outBoundary.outDeleteForm(deleteFormOut)
            outBoundary.outUpdateProgress(false)
        }
    }

    @Test
    fun `test update without permission`() = setupTest{
        coEvery { permissions.hasStoragePermission } returns false

        updateUseCase.update()
        wait()

        coVerify { outBoundary.outHasPermission(false) }
    }


    private fun TestScope.wait() {
        advanceUntilIdle()
    }

}