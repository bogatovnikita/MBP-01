package yin_kio.acceleration.presentation.acceleration.screen

import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.presentation.PermissionRequester

class AccelerationOuterImpl(
    private val navigator: AccelerationNavigator,
    private val presenter: AccelerationPresenter,
    private val permissionRequester: PermissionRequester
) : AccelerationOuter, AccelerationNavigator by navigator {


    var viewModel: MutableAccelerationViewModel? = null



    override fun showRamInfo(ramInfoOut: RamInfoOut) {
        viewModel?.setRamInfo(presenter.presentRamInfoOut(ramInfoOut))
    }

    override fun showAppsState(appsState: AppsState) {
        viewModel?.setAppsState(appsState)
    }

    override fun givePermission() {
        permissionRequester.requestPackageUsageStats()
    }
}