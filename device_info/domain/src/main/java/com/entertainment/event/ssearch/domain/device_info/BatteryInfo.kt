package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import kotlinx.coroutines.flow.StateFlow

interface BatteryInfo {

    val batteryDeviceInfo: StateFlow<DeviceFunctionGroup>

    fun stopObserve()

    fun registerBatteryReceiver()

    fun unregisterBatteryReceiver()

}