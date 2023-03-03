package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup

interface BatteryInfo {

    fun getBatteryDeviceInfo(): DeviceFunctionGroup

    fun registerBatteryReceiver()

    fun unregisterBatteryReceiver()

}