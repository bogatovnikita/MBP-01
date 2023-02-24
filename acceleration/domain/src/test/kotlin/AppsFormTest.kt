import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsFormImpl
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus

class AppsFormTest {

    private val appsForm = AppsFormImpl()

    @Test
    fun testSetApps(){
        val expected = oneApp

        appsForm.apps = expected

        assertEquals(expected, appsForm.apps)
    }

    @Test
    fun testSwitchSelectAll(){
        val input = twoApps
        appsForm.apps = input

        appsForm.switchSelectAll()

        assertTrue(isAllSelectedAndOther(input))
        assertEquals(SelectionStatus.AllSelected, appsForm.selectionStatus)

        appsForm.switchSelectAll()

        assertFalse(isAllSelectedAndOther(input))
        assertEquals(SelectionStatus.NoSelected, appsForm.selectionStatus)
    }

    private fun isAllSelectedAndOther(input: List<App>) = (appsForm.selectedApps.containsAll(input))

    @Test
    fun testSwitchSelectApp(){
        val selected = twoApps[0]
        val allApps = twoApps

        appsForm.apps = allApps

        appsForm.switchSelectApp(selected)
        assertHasSelected(selected)

        appsForm.switchSelectApp(selected)
        assertHasNotSelected(selected)

    }

    private fun assertHasNotSelected(selected: App) {
        assertFalse(appsForm.selectedApps.contains(selected)
                    || appsForm.isAppSelected(selected)
        )
        assertEquals(SelectionStatus.NoSelected, appsForm.selectionStatus)
    }

    private fun assertHasSelected(selected: App) {
        assertTrue(appsForm.selectedApps.contains(selected)
                    || appsForm.isAppSelected(selected)
        )
        assertEquals(SelectionStatus.HasSelected, appsForm.selectionStatus)
    }


}