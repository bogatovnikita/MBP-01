package com.entertainment.event.ssearch.presentation.models

data class DNDSettingsState(
    val isAutoModeSwitched: Boolean = false,
    val startTime: Int = 0,
    val endTime: Int = 0,
    val selectedDays: List<Int> = emptyList(),
    val timeSinceSelected: Boolean = false,
    val timeBeforeSelected: Boolean = false,
    val event: DNDSettingsEvent = DNDSettingsEvent.Default,
) {

    sealed class DNDSettingsEvent {

        object Default: DNDSettingsEvent()

        class SetAutoModeDND(val isSwitched: Boolean): DNDSettingsEvent()

        object SelectTimeSince: DNDSettingsEvent()

        object SelectTimeBefore: DNDSettingsEvent()

        class UpdateSelectedDayList(val days: List<Int>): DNDSettingsEvent()

        class SaveTime(val time: Int): DNDSettingsEvent()

    }

}
