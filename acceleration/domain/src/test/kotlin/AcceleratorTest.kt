import io.mockk.coVerifySequence
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.acceleration.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.AcceleratorImpl

class AcceleratorTest {

    private val accelerationOuter: AccelerationOuter = spyk()
    private val runner = AcceleratorImpl(accelerationOuter)

    @Test
    fun testRun(){
        runner.accelerate()

        coVerifySequence {
            accelerationOuter.showAccelerateProgress()
            accelerationOuter.showInter()
            accelerationOuter.complete()
        }
    }

}