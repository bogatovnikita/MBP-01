import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.bg_uploading.*
import yin_kio.acceleration.domain.gateways.Apps

class BackgroundUploadingUseCasesTest {


    private val appsForm: AppsForm = spyk()
    private val outer: BackgroundUploadingOuter = spyk()
    private val apps: Apps = spyk()
    private val useCases = BackgroundUploadingUseCases(
        outer = outer,
        appsForm = appsForm,
        apps = apps
    )

    @Test
    fun testClose(){
        useCases.close()

        coVerify { outer.close() }
    }

    @Test
    fun testSwitchSelectAllApps(){
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
            outer.updateApps()
        }
    }


    @Test
    fun testSwitchSelectApp(){
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
    fun testUpdate(){
        coEvery { apps.provide() } returns listOf()

        useCases.update()

        coVerifySequence {
            outer.setUpdateStatus(UpdateStatus.Loading)
            appsForm.apps = listOf()
            outer.setApps(listOf())
            outer.setUpdateStatus(UpdateStatus.Complete)
        }
    }

    @Test
    fun testStopSelectedApps(){
        val selectedApps = listOf("app1", "app2")

        coEvery { appsForm.selectedApps } returns selectedApps

        useCases.stopSelectedApps()

        coVerifySequence {
            outer.showStopProgress()
            apps.stop(selectedApps)
            outer.showInter()
        }
    }
}