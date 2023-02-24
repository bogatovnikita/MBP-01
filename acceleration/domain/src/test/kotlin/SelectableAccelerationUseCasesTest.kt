import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableItem
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCases
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCasesImpl

@OptIn(ExperimentalCoroutinesApi::class)
class SelectableAccelerationUseCasesTest {


    private val appsForm: AppsForm = spyk()
    private val outer: SelectableAccelerationOuter = spyk()
    private val apps: Apps = spyk()
    private val ads: Ads = spyk()
    private lateinit var useCases: SelectableAccelerationUseCases


    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        useCases = SelectableAccelerationUseCasesImpl(
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
        verifyUpdateOrderOfterSelectAll(SelectionStatus.AllSelected)
        verifyUpdateOrderOfterSelectAll(SelectionStatus.NoSelected)
    }

    private fun verifyUpdateOrderOfterSelectAll(selectionStatus: SelectionStatus) {
        coEvery { appsForm.selectionStatus } returns selectionStatus

        useCases.switchSelectAllApps()

        coVerifyOrder {
            appsForm.switchSelectAll()
            outer.setSelectionStatus(selectionStatus)
        }
    }


    @Test
    fun testSwitchSelectApp() = setupTest{
        verifyUpdateOrderAfterSelectOne(true, SelectionStatus.AllSelected)
        verifyUpdateOrderAfterSelectOne(true, SelectionStatus.HasSelected)
        verifyUpdateOrderAfterSelectOne(false, SelectionStatus.NoSelected)
    }

    private fun verifyUpdateOrderAfterSelectOne(
        hasSelected: Boolean,
        selectionStatus: SelectionStatus
    ) {
        val app = someApp
        val selectable: SelectableItem = spyk()

        coEvery { appsForm.isAppSelected(app) } returns hasSelected
        coEvery { appsForm.selectionStatus } returns selectionStatus

        useCases.switchSelectApp(app, selectable)

        coVerifyOrder {
            appsForm.switchSelectApp(app)
            selectable.setSelected(hasSelected)
            outer.setSelectionStatus(selectionStatus)
        }
    }


    @Test
    fun testUpdate() = setupTest{
        coEvery { apps.provide() } returns listOf()

        useCases.updateList()
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
        val selectedApps = twoApps

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