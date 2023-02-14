import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.use_cases.AcceleratorImpl
import yin_kio.acceleration.domain.bg_uploading.entities.AppsForm
import yin_kio.acceleration.domain.gateways.Apps

class AcceleratorTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val apps: Apps = spyk()
    private val appsForm: AppsForm = mockk()
    private val runner = AcceleratorImpl(
        accelerationOuter = accelerationOuter,
        apps = apps,
        appsForm = appsForm
    )

    @Test
    fun testAccelerate(){
        val expectedOut = listOf("app1", "app2")
        coEvery { appsForm.selectedApps } returns expectedOut

        runner.accelerate()

        coVerifySequence {
            accelerationOuter.showAccelerateProgress()
            apps.stop(expectedOut)
            accelerationOuter.showInter()
        }
    }

}