import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.applications.domain.*

class ApplicationsUseCasesTest {

    private val outer: Outer = spyk()
    private val appsInfo: AppsInfo = mockk()
    private val appsForm: AppsForm = spyk()
    private val apps: Apps = mockk()
    private val useCases = ApplicationUseCases(
        outer = outer,
        appsInfo = appsInfo,
        appsForm = appsForm,
        apps = apps
    )
    private val navigator: Navigator = spyk()
    private val selectable: Selectable = spyk()

    @Test
    fun testUpdate(){
        val appsInfoOut = AppsInfoOut()
        val systemApps = listOf(App())
        val establishedApps = listOf(App(), App())

        coEvery { appsInfo.provide() } returns appsInfoOut
        coEvery { apps.provideSystem() } returns systemApps
        coEvery { appsForm.systemApps } returns systemApps
        coEvery { apps.provideEstablished() } returns establishedApps
        coEvery { appsForm.establishedApps } returns establishedApps

        useCases.update()

        coVerify {
            appsForm.systemApps = systemApps
            appsForm.establishedApps = establishedApps

            outer.outAppsInfo(appsInfoOut)
            outer.outSystemApps(systemApps)
            outer.outEstablishedApps(establishedApps)
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
        coEvery { appsForm.isAppSelected(app) } returns isSelected

        useCases.selectApp(app, selectable)

        coVerify { selectable.setSelected(isSelected) }
    }

}