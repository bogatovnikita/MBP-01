package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.models.DeviceFunction
import javax.inject.Inject

class DeviceInfoUseCase @Inject constructor(
    private val batteryDeviceInfo: BatteryInfo
) {

    suspend fun getDeviceInfo(): List<DeviceFunction> {
        return batteryDeviceInfo.getBatteryDeviceInfo()
    }

}