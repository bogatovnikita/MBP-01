import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.use_cases.AcceleratorImpl
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm


@OptIn(ExperimentalCoroutinesApi::class)
class AcceleratorTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val apps: Apps = spyk()
    private val appsForm: AppsForm = mockk()
    private val ads: Ads = spyk()
    private val runner = AcceleratorImpl(
        accelerationOuter = accelerationOuter,
        apps = apps,
        appsForm = appsForm,
        ads = ads
    )


    @Test
    fun testAccelerate() = runTest{
        val expectedOut = twoApps
        coEvery { appsForm.selectedApps } returns expectedOut

        runner.accelerate()

        coVerifySequence {
            ads.preloadAd()
            accelerationOuter.showAccelerateProgress()
            apps.stop(expectedOut)
            accelerationOuter.showInter()
        }
    }

}