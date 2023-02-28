package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeviceInfoUseCase @Inject constructor(
    private val batteryDeviceInfo: BatteryInfo,
    private val generalDeviceInfo: GeneralDeviceInfo,
) {

    fun stopObserve() = batteryDeviceInfo.stopObserve()

    fun registerBatteryReceiver() = batteryDeviceInfo.registerBatteryReceiver()

    fun unregisterBatteryReceiver() = batteryDeviceInfo.unregisterBatteryReceiver()

    fun getDeviceInfo(): Flow<List<DeviceFunctionGroup>> = flow {

        batteryDeviceInfo.batteryDeviceInfo.collect { batteryInfo ->
            val list = listOf(
                generalDeviceInfo.getGeneralDeviceInfo(),
                batteryInfo,
            )
            emit(list)
        }
    }

}