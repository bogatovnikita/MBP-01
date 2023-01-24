import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FormItem
import yin_kio.garbage_clean.domain.entities.GarbageType

class DeleteFormTest {


    private lateinit var deleteForm: DeleteForm


    @BeforeEach
    fun setup(){
        deleteForm = DeleteForm()
    }


    @Test
    fun testUnique(){
        deleteForm.add(FormItem(GarbageType.Apk, 0))
        deleteForm.add(FormItem(GarbageType.Apk, 0))

        assertEquals(1, deleteForm.size)
    }

    @Test
    fun testCanFree(){
        deleteForm.add(FormItem(GarbageType.Apk, 5))
        deleteForm.add(FormItem(GarbageType.Temp, 5))

        assertEquals(10, deleteForm.canFree)
    }

    @Test
    fun testSwitchSelection(){
        deleteForm.add(FormItem(GarbageType.Temp, 10))
        deleteForm.add(FormItem(GarbageType.Apk, 10))

        deleteForm.switchSelection(GarbageType.Apk)
        assertTrue(deleteForm.deleteRequest.contains(GarbageType.Apk))

        deleteForm.switchSelection(GarbageType.Apk)
        assertFalse(deleteForm.deleteRequest.contains(GarbageType.Apk))
    }

    @Test
    fun testSelectAll(){

        deleteForm.selectAll()

        deleteForm.deleteRequest.apply {
            assertTrue(contains(GarbageType.Temp))
            assertTrue(contains(GarbageType.Apk))
            assertTrue(contains(GarbageType.RestFiles))
            assertTrue(contains(GarbageType.EmptyFolders))
            assertTrue(contains(GarbageType.Thumbnails))
        }
    }

}