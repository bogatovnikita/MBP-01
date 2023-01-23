import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun testSelect(){
        deleteForm.add(FormItem(GarbageType.Temp, 10))
        deleteForm.add(FormItem(GarbageType.Apk, 10))

        deleteForm.select(GarbageType.Apk)

        assertEquals(1, deleteForm.deleteRequest.size)
    }

    @Test
    fun testSelectAll(){

        deleteForm.selectAll()

        deleteForm.deleteRequest.apply {
            assertTrue(contains(GarbageType.Temp))
            assertTrue(contains(GarbageType.Apk))
            assertTrue(contains(GarbageType.Dat))
            assertTrue(contains(GarbageType.EmptyFolders))
            assertTrue(contains(GarbageType.Thumbnails))
        }
    }

}