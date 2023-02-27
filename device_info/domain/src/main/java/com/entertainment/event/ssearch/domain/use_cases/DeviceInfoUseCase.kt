package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import javax.inject.Inject

class DeviceInfoUseCase @Inject constructor(
    private val batteryDeviceInfo: BatteryInfo,
    private val generalDeviceInfo: GeneralDeviceInfo,
) {

    suspend fun getDeviceInfo(): List<DeviceFunctionGroup> {
        return listOf(
            generalDeviceInfo.getGeneralDeviceInfo(),
            batteryDeviceInfo.getBatteryDeviceInfo(),
        )
    }

}