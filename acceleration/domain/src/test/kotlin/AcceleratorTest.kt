import io.mockk.coVerifyOrder
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.AcceleratorImpl
import yin_kio.acceleration.domain.acceleration.AccelerationOuter

class AcceleratorTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val runner = AcceleratorImpl(accelerationOuter)

    @Test
    fun testRun(){
        runner.accelerate()

        coVerifyOrder {
            accelerationOuter.showAccelerateProgress()
            accelerationOuter.showInter()
            accelerationOuter.complete()
        }
    }

}