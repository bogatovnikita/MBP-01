import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.bg_uploading.entities.AppsFormImpl
import yin_kio.acceleration.domain.bg_uploading.entities.SelectionStatus

class AppsFormTest {

    private val appsForm = AppsFormImpl()

    @Test
    fun testSetApps(){
        val expected = listOf("some_app")

        appsForm.apps = expected

        assertEquals(expected, appsForm.apps)
    }

    @Test
    fun testSwitchSelectAll(){
        val expected = listOf("app1", "app2")

        appsForm.apps = expected

        appsForm.switchSelectAll()

        assertTrue(appsForm.isAllSelected)
        assertTrue(appsForm.hasSelected)
        assertTrue(appsForm.selectedApps.containsAll(expected))
        assertEquals(SelectionStatus.AllSelected, appsForm.selectionStatus)
    }

    @Test
    fun testSwitchSelectApp(){
        val selected = "app1"
        val allApps = listOf(selected, "app2")

        appsForm.apps = allApps

        appsForm.switchSelectApp(selected)

        assertFalse(appsForm.isAllSelected)
        assertTrue(appsForm.hasSelected)
        assertTrue(appsForm.selectedApps.contains(selected))
        assertEquals(SelectionStatus.HasSelected, appsForm.selectionStatus)
        assertTrue(appsForm.isAppSelected(selected))
    }

}