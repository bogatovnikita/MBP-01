import io.mockk.coVerifyOrder
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.AccelerateRunnerImpl
import yin_kio.acceleration.domain.Outer

class AccelerateRunnerTest {

    private val outer: Outer = spyk()
    private val runner = AccelerateRunnerImpl(outer)

    @Test
    fun testRun(){
        runner.run()

        coVerifyOrder {
            outer.showAccelerateProgress()
            outer.showInter()
            outer.complete()
        }
    }

}