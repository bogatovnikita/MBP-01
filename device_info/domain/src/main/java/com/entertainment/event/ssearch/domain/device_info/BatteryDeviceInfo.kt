package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunction

interface BatteryDeviceInfo {

    suspend fun getBatteryDeviceInfo(): List<DeviceFunction>
}