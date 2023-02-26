package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunction

interface BatteryInfo {

    suspend fun getBatteryDeviceInfo(): List<DeviceFunction>
}