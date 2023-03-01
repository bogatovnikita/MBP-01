import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.applications.domain.*
import yin_kio.applications.domain.core.App
import yin_kio.applications.domain.core.EstablishedAppsForm
import yin_kio.applications.domain.core.SystemAppsList
import yin_kio.applications.domain.gateways.Apps
import yin_kio.applications.domain.gateways.AppsInfo
import yin_kio.applications.domain.ui_out.AppsInfoOut
import yin_kio.applications.domain.ui_out.Navigator
import yin_kio.applications.domain.ui_out.Outer
import yin_kio.applications.domain.ui_out.Selectable

class ApplicationsUseCasesTest {

    private val outer: Outer = spyk()
    private val appsInfo: AppsInfo = mockk()
    private val establishedAppsForm: EstablishedAppsForm = spyk()
    private val apps: Apps = mockk()
    private val systemAppsList: SystemAppsList = spyk()
    private val useCases = ApplicationUseCases(
        outer = outer,
        appsInfo = appsInfo,
        establishedAppsForm = establishedAppsForm,
        apps = apps,
        systemAppsList = systemAppsList
    )
    private val navigator: Navigator = spyk()
    private val selectable: Selectable = spyk()

    @Test
    fun testUpdateAppsInfo(){
        val appsInfoOut = AppsInfoOut()
        coEvery { appsInfo.provide() } returns appsInfoOut

        useCases.updateAppsInfo()

        coVerify { outer.outAppsInfo(appsInfoOut) }
    }

    @Test
    fun testUpdateEstablishedApps(){
        val establishedApps = listOf(App())

        coEvery { apps.provideEstablished() } returns establishedApps
        coEvery { establishedAppsForm.content } returns establishedApps

        useCases.updateEstablishedApps()

        coVerify {
            establishedAppsForm.content = establishedApps
            establishedAppsForm.isVisible = true
            outer.outEstablishedApps(establishedApps)
            outer.expandEstablishedApps()
        }
    }

    @Test
    fun testUpdateSystemApps(){
        val systemApps = listOf(App())

        coEvery { apps.provideSystem() } returns systemApps
        coEvery { systemAppsList.content } returns systemApps

        useCases.updateSystemApps()

        coVerify {
            systemAppsList.content = systemApps
            systemAppsList.isVisible = true
            outer.outSystemApps(systemApps)
            outer.expandSystemApps()
        }
    }

    @Test
    fun testClose(){
        useCases.close(navigator)

        coVerify { navigator.close() }
    }

    @Test
    fun testAskDelete(){
        useCases.askDelete(navigator)

        coVerify { navigator.showAskDeleteDialog() }
    }

    @Test
    fun testDelete(){
        useCases.delete(navigator)

        coVerifySequence {
            navigator.showDeleteProgressDialog()
            navigator.showInter()
        }
    }

    @Test
    fun testComplete(){
        useCases.complete(navigator)

        coVerify { navigator.complete() }
    }

    @Test
    fun testSelectApp(){
        assertAppSelected(true)
        assertAppSelected(false)
    }

    private fun assertAppSelected(isSelected: Boolean) {
        val app = App()
        coEvery { establishedAppsForm.isAppSelected(app) } returns isSelected

        useCases.selectApp(app, selectable)

        coVerify { selectable.setSelected(isSelected) }
    }

    @Test
    fun testSortSystemApps(){
        val sortedApps: List<App> = listOf()
        coEvery { systemAppsList.content } returns sortedApps

        useCases.sortSystemApps()

        coVerify {
            systemAppsList.sort()
            outer.outSystemApps(sortedApps)
        }
    }

    @Test
    fun testSortEstablishedApps() {
        val sortedApps: List<App> = listOf()
        coEvery { establishedAppsForm.content } returns sortedApps

        useCases.sortEstablishedApps()

        coVerify {
            establishedAppsForm.sort()
            outer.outEstablishedApps(sortedApps)
        }
    }

    @Test
    fun testCollapseSystemApps(){
        useCases.collapseSystemApps()

        coVerify {
            systemAppsList.isVisible = false
            outer.collapseSystemApps()
        }
    }

}