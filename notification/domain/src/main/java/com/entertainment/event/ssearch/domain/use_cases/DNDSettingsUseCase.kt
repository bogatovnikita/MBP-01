package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.dnd.DNDAutoModeController
import com.entertainment.event.ssearch.domain.dnd.DNDSettings
import com.entertainment.event.ssearch.domain.dnd.DayPickerSettings
import com.entertainment.event.ssearch.domain.dnd.TimeSettings
import javax.inject.Inject

class DNDSettingsUseCase @Inject constructor(
    private val dndSettings: DNDSettings,
    private val timeSettings: TimeSettings,
    private val dayPickerSettings: DayPickerSettings,
    private val dndAutoModeController: DNDAutoModeController,
) {

    suspend fun isAutoModeSwitched() = dndSettings.isAutoModeDNDEnabled()

    suspend fun setAutoModeDND(isSwitched: Boolean) = dndSettings.setAutoModeDND(isSwitched)

    suspend fun getSelectedDays(): List<Int> {
        return mutableListOf<Int>().apply {
            if (dayPickerSettings.isMondayIncluded()) add(MONDAY)
            if (dayPickerSettings.isTuesdayIncluded()) add(TUESDAY)
            if (dayPickerSettings.isWednesdayIncluded()) add(WEDNESDAY)
            if (dayPickerSettings.isThursdayIncluded()) add(THURSDAY)
            if (dayPickerSettings.isFridayIncluded()) add(FRIDAY)
            if (dayPickerSettings.isSaturdayIncluded()) add(SATURDAY)
            if (dayPickerSettings.isSundayIncluded()) add(SUNDAY)
        }
    }

    fun setRepeatAlarmStart(
        hours: Int,
        minutes: Int,
        daysOfWeek: List<Int>,
    ) {
        dndAutoModeController.setRepeatAlarm(
            hours = hours,
            minutes = minutes,
            daysOfWeek = daysOfWeek,
            action = DND_ON
        )
    }

    fun setRepeatAlarmEnd(
        hours: Int,
        minutes: Int,
        daysOfWeek: List<Int>,
    ) {
        dndAutoModeController.setRepeatAlarm(
            hours = hours,
            minutes = minutes,
            daysOfWeek = daysOfWeek,
            action = DND_OFF
        )
    }

    suspend fun getStartTime() = timeSettings.getStartTime()

    suspend fun setStartTime(time: Int) = timeSettings.setStartTime(time)

    suspend fun getEndTime() = timeSettings.getEndTime()

    suspend fun setEndTime(time: Int) = timeSettings.setEndTime(time)

    companion object {
        const val MONDAY = 2
        const val TUESDAY = 3
        const val WEDNESDAY = 4
        const val THURSDAY = 5
        const val FRIDAY = 6
        const val SATURDAY = 7
        const val SUNDAY = 1

        const val DND_ON = 1212
        const val DND_OFF = 3232
    }
}