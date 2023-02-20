package yin_kio.acceleration.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.Test
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.presentation.acceleration.AccelerationOuterImpl
import yin_kio.acceleration.presentation.acceleration.AccelerationPresenter
import yin_kio.acceleration.presentation.acceleration.MutableAccelerationViewModel
import yin_kio.acceleration.presentation.acceleration.RamInfo


class AccelerationOuterTest {

    private val navigator: AccelerationNavigator = spyk()
    private val viewModel: MutableAccelerationViewModel = spyk()
    private val presenter: AccelerationPresenter = spyk()
    private val permissionRequester: PermissionRequester = spyk()
    private val outer = AccelerationOuterImpl(
        navigator = navigator,
        viewModel = viewModel,
        presenter = presenter,
        permissionRequester = permissionRequester
    )

    @Test
    fun testShowRamInfo(){
        val ramInfo = RamInfo()
        val ramInfoOut = RamInfoOut()

        coEvery { presenter.presentRamInfoOut(ramInfoOut) } returns ramInfo

        outer.showRamInfo(ramInfoOut)

        coVerify { viewModel.setRamInfo(ramInfo) }
    }

    @Test
    fun testShowAppsState(){
        val appsState = AppsState.Progress

        outer.showAppsState(appsState)

        coVerify { viewModel.setAppsState(appsState) }
    }

    @Test
    fun testGivePermission(){
        outer.givePermission()

        coVerify { permissionRequester.requestPackageUsageStats() }
    }

}