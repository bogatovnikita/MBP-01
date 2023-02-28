package yin_kio.acceleration.presentation.acceleration.presenter

import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.presentation.acceleration.view_model.RamInfo

interface AccelerationPresenter {

    fun presentRamInfoOut(ramInfoOut: RamInfoOut) : RamInfo

}