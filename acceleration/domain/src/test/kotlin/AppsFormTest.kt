import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsFormImpl
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus

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
        val input = listOf("app1", "app2")
        appsForm.apps = input

        appsForm.switchSelectAll()

        assertTrue(isAllSelectedAndOther(input))
        assertEquals(SelectionStatus.AllSelected, appsForm.selectionStatus)

        appsForm.switchSelectAll()

        assertFalse(isAllSelectedAndOther(input))
        assertEquals(SelectionStatus.NoSelected, appsForm.selectionStatus)
    }

    private fun isAllSelectedAndOther(input: List<String>) = (appsForm.selectedApps.containsAll(input))

    @Test
    fun testSwitchSelectApp(){
        val selected = "app1"
        val allApps = listOf(selected, "app2")

        appsForm.apps = allApps

        appsForm.switchSelectApp(selected)
        assertHasSelected(selected)

        appsForm.switchSelectApp(selected)
        assertHasNotSelected(selected)

    }

    private fun assertHasNotSelected(selected: String) {
        assertFalse(appsForm.selectedApps.contains(selected)
                    || appsForm.isAppSelected(selected)
        )
        assertEquals(SelectionStatus.NoSelected, appsForm.selectionStatus)
    }

    private fun assertHasSelected(selected: String) {
        assertTrue(appsForm.selectedApps.contains(selected)
                    || appsForm.isAppSelected(selected)
        )
        assertEquals(SelectionStatus.HasSelected, appsForm.selectionStatus)
    }


}