package yin_kio.acceleration.domain.acceleration.gateways

import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut

interface RamInfo {

    fun provide() : RamInfoOut

}