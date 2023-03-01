package yin_kio.applications.domain.gateways

import yin_kio.applications.domain.ui_out.AppsInfoOut

interface AppsInfo {

    fun provide() : AppsInfoOut

}