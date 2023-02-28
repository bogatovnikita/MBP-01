package yin_kio.acceleration.presentation.acceleration.view_model

import yin_kio.acceleration.domain.acceleration.ui_out.AppsState

interface MutableAccelerationViewModel {

    fun setRamInfo(ramInfo: RamInfo)
    fun setAppsState(appsState: AppsState)

}