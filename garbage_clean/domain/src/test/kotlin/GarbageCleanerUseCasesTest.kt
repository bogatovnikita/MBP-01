import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.GarbageCleanerUseCases
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.GarbageType

class GarbageCleanerUseCasesTest {

    private val deleteForm: DeleteForm = mockk()
    private val useCases = GarbageCleanerUseCases(deleteForm)


    @Test
    fun testSelectAll(){
        coEvery { deleteForm.selectAll() } returns Unit

        useCases.selectAll()

        coVerify { deleteForm.selectAll() }
    }

    @Test
    fun testSwitchSelection(){
        coEvery { deleteForm.switchSelection(GarbageType.Apk) } returns Unit

        useCases.switchSelection(GarbageType.Apk)

        coVerify { deleteForm.switchSelection(GarbageType.Apk) }
    }

}