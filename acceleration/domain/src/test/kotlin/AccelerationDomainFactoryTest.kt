import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import yin_kio.acceleration.domain.AccelerationDomainFactory

class AccelerationDomainFactoryTest {

    @Test
    fun testCreateAccelerationUseCases(){
        assertDoesNotThrow {
            AccelerationDomainFactory.createAccelerationUseCases(
                mockk(),
                mockk(),
                mockk(),
                mockk(),
                mockk()
            )
        }

    }

}