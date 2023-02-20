package yin_kio.acceleration.presentation.acceleration.screen

import yin_kio.acceleration.domain.acceleration.ui_out.AppsState

interface MutableAccelerationViewModel {

    fun setRamInfo(ramInfo: RamInfo)
    fun setAppsState(appsState: AppsState)

}