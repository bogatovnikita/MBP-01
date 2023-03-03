package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.device_info.FunctionalityInfo
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import com.entertainment.event.ssearch.domain.device_info.ProcessorInfo
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeviceInfoUseCase @Inject constructor(
    private val processorInfo: ProcessorInfo,
    private val batteryDeviceInfo: BatteryInfo,
    private val generalDeviceInfo: GeneralDeviceInfo,
    private val functionalityInfo: FunctionalityInfo,
) {

    private var needObserve = true

    fun startObserve() {
        needObserve = true
    }

    fun stopObserve() {
        needObserve = false
    }

    fun registerBatteryReceiver() = batteryDeviceInfo.registerBatteryReceiver()

    fun unregisterBatteryReceiver() = batteryDeviceInfo.unregisterBatteryReceiver()

    fun getDeviceInfo(): Flow<List<DeviceFunctionGroup>> = flow {
        while (true) {
            if (needObserve) {
                val list = listOf(
                    generalDeviceInfo.getGeneralDeviceInfo(),
                    functionalityInfo.getFunctionalityInfo(),
                    processorInfo.getProcessorInfo(),
                    batteryDeviceInfo.getBatteryDeviceInfo(),
                )
                emit(list)
            }
            delay(1000)
        }
    }

}