import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.*
import yin_kio.acceleration.domain.gateways.Apps

class AccelerationUseCasesTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val permissions: Permissions = spyk()
    private val accelerator: Accelerator = spyk()
    private val ramInfo: RamInfo = spyk()
    private val apps: Apps = spyk()
    private val useCases = AccelerationUseCasesImpl(
        accelerationOuter = accelerationOuter,
        permissions = permissions,
        runner = accelerator,
        ramInfo = ramInfo,
        apps = apps
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
        val appsList = listOf("app1", "app2")

        coEvery { ramInfo.provide() } returns  ramInfoOut
        coEvery { apps.provide() } returns appsList

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

    @Test
    fun testComplete(){
        useCases.complete()

        coVerify(exactly = 1) { accelerationOuter.complete() }
    }












    private fun ifHasNotPermission(
        useCase: AccelerationUseCasesImpl.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns false

        useCases.useCase()

        coVerify { accelerationOuter.outs() }
    }

    private fun ifHasPermission(
        usaCase: AccelerationUseCasesImpl.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns true

        useCases.usaCase()

        coVerify { accelerationOuter.outs() }
    }


}