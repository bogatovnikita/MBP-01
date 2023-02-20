package yin_kio.acceleration.presentation.acceleration

import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut

interface AccelerationPresenter {

    fun presentRamInfoOut(ramInfoOut: RamInfoOut) : RamInfo

}