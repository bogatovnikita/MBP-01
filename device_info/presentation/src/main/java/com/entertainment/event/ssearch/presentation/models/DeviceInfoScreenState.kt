package com.entertainment.event.ssearch.presentation.models

import com.entertainment.event.ssearch.domain.models.DeviceFunction

data class DeviceInfoScreenState(
    val deviceInfo: List<DeviceFunction> = emptyList(),
    val event: DeviceInfoEvent = DeviceInfoEvent.Default
) {

    sealed class DeviceInfoEvent {

        object Default: DeviceInfoEvent()

    }

}