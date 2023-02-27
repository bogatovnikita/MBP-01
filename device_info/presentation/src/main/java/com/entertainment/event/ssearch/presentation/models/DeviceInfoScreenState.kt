package com.entertainment.event.ssearch.presentation.models

import com.entertainment.event.ssearch.domain.models.DeviceFunction
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup

data class DeviceInfoScreenState(
    val showedDeviceInfo: List<DeviceFunction> = emptyList(),
    val mainDeviceInfo: List<DeviceFunctionGroup> = emptyList(),
    val event: DeviceInfoEvent = DeviceInfoEvent.Default
) {

    sealed class DeviceInfoEvent {

        object Default: DeviceInfoEvent()

    }

}