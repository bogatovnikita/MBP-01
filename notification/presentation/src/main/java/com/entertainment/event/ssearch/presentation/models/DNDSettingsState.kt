package com.entertainment.event.ssearch.presentation.models

data class DNDSettingsState(
    val endTime: Int = 0,
    val startTime: Int = 0,
    val timeStartSelected: Boolean = false,
    val timeEndSelected: Boolean = false,
    val isAutoModeSwitched: Boolean = false,
    val selectedDays: List<Int> = emptyList(),
    val event: DNDSettingsEvent = DNDSettingsEvent.Default,
)

sealed class DNDSettingsEvent {

    object Default: DNDSettingsEvent()

    class SetAutoModeDND(val isSwitched: Boolean): DNDSettingsEvent()

    object SelectStartTime: DNDSettingsEvent()

    object SelectEndTime: DNDSettingsEvent()

    class UpdateSelectedDayList(val days: List<Int>): DNDSettingsEvent()

    object WrongFormat: DNDSettingsEvent()

    class SaveTime(val time: Int): DNDSettingsEvent()

}