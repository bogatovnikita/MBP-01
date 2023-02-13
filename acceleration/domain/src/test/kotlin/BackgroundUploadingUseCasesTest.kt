import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.bg_uploading.AppsForm
import yin_kio.acceleration.domain.bg_uploading.BackgroundUploadingOuter
import yin_kio.acceleration.domain.bg_uploading.BackgroundUploadingUseCases

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
        verifyUpdateOrderOfterSelectAll(true)
        verifyUpdateOrderOfterSelectAll(false)
    }

    private fun verifyUpdateOrderOfterSelectAll(hasSelected: Boolean) {
        coEvery { appsForm.hasSelected } returns hasSelected
        useCases.switchSelectAllApps()

        coVerifyOrder {
            appsForm.switchSelectAll()
            outer.setButtonEnabled(hasSelected)
            outer.updateApps()
        }
    }


    @Test
    fun testSwitchSelectApp(){
        verifyUpdateOrderAfterSelectOne(true, true)
        verifyUpdateOrderAfterSelectOne(true, false)
        verifyUpdateOrderAfterSelectOne(false, false)
    }

    private fun verifyUpdateOrderAfterSelectOne(hasSelected: Boolean, isAllSelected: Boolean) {
        val packageName = "some_name"

        coEvery { appsForm.isAppSelected(packageName) } returns hasSelected
        coEvery { appsForm.hasSelected } returns hasSelected
        coEvery { appsForm.isAllSelected } returns isAllSelected

        useCases.switchSelectApp(packageName)

        coVerifyOrder {
            appsForm.switchSelectApp(packageName)
            outer.setAppSelected(packageName, hasSelected)
            outer.setButtonEnabled(hasSelected)
            outer.setAllSelected(isAllSelected)
        }
    }

}