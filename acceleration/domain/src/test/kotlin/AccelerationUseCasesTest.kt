import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.*

class AccelerationUseCasesTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val permissions: Permissions = spyk()
    private val accelerator: Accelerator = spyk()
    private val ramInfo: RamInfo = spyk()
    private val useCases = AccelerationUseCases(
        accelerationOuter = accelerationOuter,
        permissions = permissions,
        runner = accelerator,
        ramInfo = ramInfo
    )

    @Test
    fun testClose(){
        useCases.close()

        coVerify { accelerationOuter.close() }
    }

    @Test
    fun testAccelerate(){
        verifyRunAccelerate()
        verifyShowPermission{ useCases.accelerate() }

    }

    private fun verifyRunAccelerate() {
        coEvery { permissions.hasPermission } returns true

        useCases.accelerate()

        coVerify { accelerator.accelerate() }
    }

    @Test
    fun testUploadBackgroundProcess(){
        ifHasNotPermission(
            useCase = { uploadBackgroundProcess() },
            outs = { showPermission() }
        )
        ifHasPermission(
            usaCase = { uploadBackgroundProcess() },
            outs = { showSelectableAcceleration()  }
        )
    }

    private fun verifyShowPermission(
        action: () -> Unit
    ) {
        coEvery { permissions.hasPermission } returns false

        action()

        coVerify { accelerationOuter.showPermission() }
    }


    @Test
    fun testUpdate(){
        val ramInfoOut = RamInfoOut()

        coEvery { ramInfo.provide() } returns  ramInfoOut

        val appsList = listOf<String>()
        ifHasPermission(
            usaCase = { update() },
            outs = {
                showRamInfo(ramInfoOut)
                showAppsList(appsList)
            }
        )

        ifHasNotPermission(
            useCase = { update() },
            outs = {
                showRamInfo(ramInfoOut)
                showPermissionOnList()
            }
        )

    }












    private fun ifHasNotPermission(
        useCase: AccelerationUseCases.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns false

        useCases.useCase()

        coVerify { accelerationOuter.outs() }
    }

    private fun ifHasPermission(
        usaCase: AccelerationUseCases.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns true

        useCases.usaCase()

        coVerify { accelerationOuter.outs() }
    }


}