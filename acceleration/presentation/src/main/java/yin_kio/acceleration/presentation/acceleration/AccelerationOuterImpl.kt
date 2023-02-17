package yin_kio.acceleration.presentation.acceleration

import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut

class AccelerationOuterImpl(
    private val navigator: AccelerationNavigator,
    private val viewModel: MutableAccelerationViewModel,
    private val presenter: AccelerationPresenter
) : AccelerationOuter, AccelerationNavigator by navigator {



    override fun showRamInfo(ramInfoOut: RamInfoOut) {
        viewModel.setRamInfo(presenter.presentRamInfoOut(ramInfoOut))
    }

    override fun showAppsState(appsState: AppsState) {
        TODO("Not yet implemented")
    }

    override fun givePermission() {
        TODO("Not yet implemented")
    }
}