package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup

interface BatteryInfo {

    suspend fun getBatteryDeviceInfo(): DeviceFunctionGroup

}