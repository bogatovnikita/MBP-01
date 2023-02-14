import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.bg_uploading.AppsForm
import yin_kio.acceleration.domain.bg_uploading.BackgroundUploadingOuter
import yin_kio.acceleration.domain.bg_uploading.BackgroundUploadingUseCases
import yin_kio.acceleration.domain.bg_uploading.SelectionStatus

class BackgroundUploadingUseCasesTest {


    private val appsForm: AppsForm = spyk()
    private val outer: BackgroundUploadingOuter = spyk()
    private val useCases = BackgroundUploadingUseCases(
        outer = outer,
        appsForm = appsForm
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

}