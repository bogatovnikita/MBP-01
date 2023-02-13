import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.*

class AccelerationUseCasesTest {

    private val outer: Outer = spyk()
    private val permissions: Permissions = spyk()
    private val accelerateRunner: AccelerateRunner = spyk()
    private val ramInfo: RamInfo = spyk()
    private val useCases = AccelerationUseCases(
        outer = outer,
        permissions = permissions,
        runner = accelerateRunner,
        ramInfo = ramInfo
    )

    @Test
    fun testClose(){
        useCases.close()

        coVerify { outer.close() }
    }

    @Test
    fun testAccelerate(){
        verifyRunAccelerate()
        verifyShowPermission{ useCases.accelerate() }

    }

    private fun verifyRunAccelerate() {
        coEvery { permissions.hasPermission } returns true

        useCases.accelerate()

        coVerify { accelerateRunner.run() }
    }

    @Test
    fun testUploadBackgroundProcess(){
        verifyShowPermission { useCases.uploadBackgroundProcess() }
        verifyShowSelectableAcceleration()
    }

    private fun verifyShowPermission(
        action: () -> Unit
    ) {
        coEvery { permissions.hasPermission } returns false

        action()

        coVerify { outer.showPermission() }
    }

    private fun verifyShowSelectableAcceleration() {
        coEvery { permissions.hasPermission } returns true

        useCases.uploadBackgroundProcess()

        coVerify { outer.showSelectableAcceleration() }
    }


    @Test
    fun testUpdate(){
        val ramInfoOut = RamInfoOut()

        coEvery { ramInfo.provide() } returns  ramInfoOut

        useCases.update()

        coVerify { outer.showRamInfo(ramInfoOut) }

    }


}