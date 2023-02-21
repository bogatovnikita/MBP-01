import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.BackgroundUploadingUseCases
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.BackgroundUploadingUseCasesImpl
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps

@OptIn(ExperimentalCoroutinesApi::class)
class BackgroundUploadingUseCasesTest {


    private val appsForm: AppsForm = spyk()
    private val outer: SelectableAccelerationOuter = spyk()
    private val apps: Apps = spyk()
    private val ads: Ads = spyk()
    private lateinit var useCases: BackgroundUploadingUseCases


    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        useCases = BackgroundUploadingUseCasesImpl(
            outer = outer,
            appsForm = appsForm,
            apps = apps,
            coroutineScope = this,
            dispatcher = coroutineContext,
            ads = ads
        )

        testBlock()
    }


    @Test
    fun testClose() = setupTest{
        useCases.close()

        coVerify { outer.close() }
    }

    @Test
    fun testSwitchSelectAllApps() = setupTest{
        verifyUpdateOrderOfterSelectAll(true, SelectionStatus.AllSelected)
        verifyUpdateOrderOfterSelectAll(false, SelectionStatus.NoSelected)
    }

    private fun verifyUpdateOrderOfterSelectAll(hasSelected: Boolean, selectionStatus: SelectionStatus) {
        coEvery { appsForm.hasSelected } returns hasSelected
        coEvery { appsForm.selectionStatus } returns selectionStatus

        useCases.switchSelectAllApps()

        coVerifyOrder {
            appsForm.switchSelectAll()
            outer.setSelectionStatus(selectionStatus)
        }
    }


    @Test
    fun testSwitchSelectApp() = setupTest{
        verifyUpdateOrderAfterSelectOne(true, true, SelectionStatus.AllSelected)
        verifyUpdateOrderAfterSelectOne(true, false, SelectionStatus.HasSelected)
        verifyUpdateOrderAfterSelectOne(false, false, SelectionStatus.NoSelected)
    }

    private fun verifyUpdateOrderAfterSelectOne(
        hasSelected: Boolean,
        isAllSelected: Boolean,
        selectionStatus: SelectionStatus
    ) {
        val packageName = "some_name"

        coEvery { appsForm.isAppSelected(packageName) } returns hasSelected
        coEvery { appsForm.hasSelected } returns hasSelected
        coEvery { appsForm.isAllSelected } returns isAllSelected
        coEvery { appsForm.selectionStatus } returns selectionStatus

        useCases.switchSelectApp(packageName)

        coVerifyOrder {
            appsForm.switchSelectApp(packageName)
            outer.setAppSelected(packageName, hasSelected)
            outer.setSelectionStatus(selectionStatus)
        }
    }


    @Test
    fun testUpdate() = setupTest{
        coEvery { apps.provide() } returns listOf()

        useCases.update()
        wait()

        coVerifySequence {
            outer.setUpdateStatus(UpdateStatus.Loading)
            appsForm.apps = listOf()
            outer.setApps(listOf())
            outer.setUpdateStatus(UpdateStatus.Complete)
        }
    }

    @Test
    fun testStopSelectedApps() = setupTest{
        val selectedApps = listOf("app1", "app2")

        coEvery { appsForm.selectedApps } returns selectedApps

        useCases.stopSelectedApps()
        wait()

        coVerifySequence {
            ads.preloadAd()
            outer.showStopProgress()
            apps.stop(selectedApps)
            outer.showInter()
        }
    }

    @Test
    fun testComplete() = setupTest{
        useCases.complete()
        wait()

        coVerify( exactly = 1 ) { outer.complete() }
    }


    private fun TestScope.wait() {
        advanceUntilIdle()
    }
}