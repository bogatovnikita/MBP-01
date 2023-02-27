import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
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
        verifyUpdateOrderOfterSelectAll(SelectionStatus.AllSelected, selected = twoApps)
        verifyUpdateOrderOfterSelectAll(SelectionStatus.NoSelected, selected = noApps)
    }

    private fun verifyUpdateOrderOfterSelectAll(selectionStatus: SelectionStatus, selected: List<App>) {
        coEvery { appsForm.selectionStatus } returns selectionStatus
        coEvery { appsForm.selectedApps } returns selected

        useCases.switchSelectAllApps()

        coVerifyOrder {
            appsForm.switchSelectAll()
            outer.setSelectionStatus(selectionStatus)
            outer.setSelectedApps(selected)
        }
    }


    @Test
    fun testSwitchSelectApp() = setupTest{
        verifyUpdateOrderAfterSelectOne(true, SelectionStatus.AllSelected,
            selectedApps = twoApps
        )
        verifyUpdateOrderAfterSelectOne(true, SelectionStatus.HasSelected,
            selectedApps = oneApp
        )
        verifyUpdateOrderAfterSelectOne(false, SelectionStatus.NoSelected,
            selectedApps = emptyList()
        )
    }

    private fun verifyUpdateOrderAfterSelectOne(
        hasSelected: Boolean,
        selectionStatus: SelectionStatus,
        selectedApps: List<App>
    ) {
        val app = someApp
        val selectable: SelectableItem = spyk()

        coEvery { appsForm.selectedApps } returns selectedApps
        coEvery { appsForm.isAppSelected(app) } returns hasSelected
        coEvery { appsForm.selectionStatus } returns selectionStatus

        useCases.switchSelectApp(app, selectable)

        coVerifyOrder {
            appsForm.switchSelectApp(app)
            selectable.setSelected(hasSelected)
            outer.setSelectionStatus(selectionStatus)
            outer.setSelectedApps(selectedApps)
        }
    }


    @Test
    fun testUpdate() = setupTest{
        val outApps = listOf(someApp)
        coEvery { apps.provide() } returns outApps
        coEvery { appsForm.selectedApps } returns outApps

        useCases.updateList()
        wait()

        coVerifyOrder {
            outer.setUpdateStatus(UpdateStatus.Loading)
            appsForm.apps = outApps
            outer.setSelectedApps(outApps)
            outer.setUpdateStatus(UpdateStatus.Complete)
        }
    }

    @Test
    fun testStopSelectedApps() = setupTest{
        assertNotStopIfHasNotSelected()
        assertStopIfHasSelected()
    }

    private fun TestScope.assertNotStopIfHasNotSelected() {
        coEvery { appsForm.selectedApps } returns emptyList()
        coEvery { appsForm.selectionStatus } returns SelectionStatus.NoSelected

        useCases.stopSelectedApps()
        wait()

        coVerify(inverse = true) { apps.stop(appsForm.selectedApps) }
    }

    private fun TestScope.assertStopIfHasSelected() {
        val selectedApps = twoApps

        coEvery { appsForm.selectionStatus } returns SelectionStatus.HasSelected
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

    @Test
    fun testUpdateAppItem() = setupTest{
        assertAppUpdated(true)
        assertAppUpdated(false)
    }

    private fun assertAppUpdated(isSelected: Boolean) {
        coEvery { appsForm.isAppSelected(App()) } returns isSelected
        val selectable: SelectableItem = spyk()

        useCases.updateAppItem(App(), selectable)

        coVerify { selectable.setSelected(isSelected) }
    }

    private fun TestScope.wait() {
        advanceUntilIdle()
    }
}