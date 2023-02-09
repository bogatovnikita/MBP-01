import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.memory.domain.*


@OptIn(ExperimentalCoroutinesApi::class)
class MemoryUseCasesTest {
    private val outBoundary: OutBoundary = spyk()
    private val ramInfo: RamInfo = spyk()
    private val storageInfo: StorageInfo = spyk()

    private val ramInfoOut = MemoryInfoOut()
    private val storageInfoOut = MemoryInfoOut()


    @Test
    fun testUpdate() = runTest{
        coEvery { ramInfo.provide() } returns ramInfoOut
        coEvery { storageInfo.provide() } returns storageInfoOut

        memoryUseCases().update()
        advanceUntilIdle() // wait

        coVerify { outBoundary.outMemoryInfos(ramInfoOut, storageInfoOut) }
    }

    private fun TestScope.memoryUseCases(): MemoryUseCases {
        return MemoryUseCases(
            outBoundary = outBoundary,
            ramInfo = ramInfo,
            storageInfo = storageInfo,
            coroutineScope = this,
            dispatcher = coroutineContext
        )
    }

}