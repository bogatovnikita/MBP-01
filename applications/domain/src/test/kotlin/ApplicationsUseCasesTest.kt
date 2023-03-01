import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.applications.domain.*

class ApplicationsUseCasesTest {

    private val outer: Outer = spyk()
    private val appsInfo: AppsInfo = mockk()
    private val useCases = ApplicationUseCases(outer, appsInfo)
    private val navigator: Navigator = spyk()

    @Test
    fun testUpdate(){
        val appsInfoOut = AppsInfoOut()
        coEvery { appsInfo.provide() } returns appsInfoOut

        useCases.update()

        coVerify { outer.outAppsInfo(appsInfoOut) }
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

}