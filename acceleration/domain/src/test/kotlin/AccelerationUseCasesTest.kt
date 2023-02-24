import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.gateways.Permissions
import yin_kio.acceleration.domain.acceleration.gateways.RamInfo
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCases
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCasesImpl
import yin_kio.acceleration.domain.acceleration.use_cases.Accelerator
import yin_kio.acceleration.domain.gateways.Apps

@OptIn(ExperimentalCoroutinesApi::class)
class AccelerationUseCasesTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val permissions: Permissions = spyk()
    private val accelerator: Accelerator = spyk()
    private val ramInfo: RamInfo = spyk()
    private val apps: Apps = spyk()
    private lateinit var useCases: AccelerationUseCases


    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        useCases = AccelerationUseCasesImpl(
            accelerationOuter = accelerationOuter,
            permissions = permissions,
            runner = accelerator,
            ramInfo = ramInfo,
            apps = apps,
            coroutineScope = this,
            dispatcher = coroutineContext
        )

        testBlock()
    }

    @Test
    fun testClose() = setupTest {
        useCases.close()
        wait()

        coVerify { accelerationOuter.close() }
    }


    @Test
    fun testAccelerate() = setupTest {
        verifyRunAccelerate()
        verifyShowPermission{ useCases.accelerate() }

    }

    private fun TestScope.verifyRunAccelerate() {
        coEvery { permissions.hasPermission } returns true

        useCases.accelerate()
        wait()

        coVerify { accelerator.accelerate() }
    }

    @Test
    fun testUploadBackgroundProcess() = setupTest {
        ifHasNotPermission(
            useCase = { uploadBackgroundProcess() },
            outs = { showPermission() }
        )
        ifHasPermission(
            usaCase = { uploadBackgroundProcess() },
            outs = { showSelectableAcceleration()  }
        )
    }

    private fun TestScope.verifyShowPermission(
        action: () -> Unit
    ) {
        coEvery { permissions.hasPermission } returns false

        action()
        wait()

        coVerify { accelerationOuter.showPermission() }
    }


    @Test
    fun testUpdate() = setupTest {
        val ramInfoOut = RamInfoOut()
        val appsList = twoApps

        coEvery { ramInfo.provide() } returns  ramInfoOut
        coEvery { apps.provide() } returns appsList

        ifHasPermission(
            usaCase = { update() },
            outs = {
                showRamInfo(ramInfoOut)
                showAppsState(AppsState.Progress)
                showAppsState(AppsState.AppsList(appsList))
            }
        )

        ifHasNotPermission(
            useCase = { update() },
            outs = {
                showRamInfo(ramInfoOut)
                showAppsState(AppsState.Progress)
                showAppsState(AppsState.Permission)
            }
        )

    }

    @Test
    fun testComplete() = setupTest {
        useCases.complete()
        wait()

        coVerify(exactly = 1) { accelerationOuter.complete() }
    }


    @Test
    fun testGivePermission() = setupTest {
        useCases.givePermission()
        wait()

        coVerify(exactly = 1) { accelerationOuter.givePermission() }
    }









    private fun TestScope.ifHasNotPermission(
        useCase: AccelerationUseCases.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns false

        useCases.useCase()
        wait()

        coVerify { accelerationOuter.outs() }
    }

    private fun TestScope.ifHasPermission(
        usaCase: AccelerationUseCases.() -> Unit,
        outs: AccelerationOuter.() -> Unit
    ){
        coEvery { permissions.hasPermission } returns true

        useCases.usaCase()
        wait()

        coVerify { accelerationOuter.outs() }
    }


    private fun TestScope.wait() {
        advanceUntilIdle()
    }


}