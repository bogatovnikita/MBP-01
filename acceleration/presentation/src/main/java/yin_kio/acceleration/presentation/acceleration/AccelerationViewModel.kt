package yin_kio.acceleration.presentation.acceleration

import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCases

class AccelerationViewModel(
    private val useCases: AccelerationUseCases
) : MutableAccelerationViewModel, AccelerationUseCases by useCases {

    override fun setRamInfo(ramInfo: RamInfo) {
        TODO("Not yet implemented")
    }

    override fun setAppsState(appsState: AppsState) {
        TODO("Not yet implemented")
    }
}