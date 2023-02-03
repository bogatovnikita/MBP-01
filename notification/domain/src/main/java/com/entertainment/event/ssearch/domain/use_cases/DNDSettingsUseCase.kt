package com.entertainment.event.ssearch.domain.use_cases

import com.entertainment.event.ssearch.domain.dnd.DNDSettings
import com.entertainment.event.ssearch.domain.dnd.DayPickerSettings
import com.entertainment.event.ssearch.domain.dnd.TimeSettings
import javax.inject.Inject

class DNDSettingsUseCase @Inject constructor(
    private val dndSettings: DNDSettings,
    private val timeSettings: TimeSettings,
    private val dayPickerSettings: DayPickerSettings
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

    suspend fun getStartTime() = timeSettings.getStartTime()

    suspend fun setStartTime(time: Int) = timeSettings.setStartTime(time)

    suspend fun getEndTime() = timeSettings.getEndTime()

    suspend fun setEndTime(time: Int) = timeSettings.setEndTime(time)

    companion object {
        const val MONDAY = 1
        const val TUESDAY = 2
        const val WEDNESDAY = 3
        const val THURSDAY = 4
        const val FRIDAY = 5
        const val SATURDAY = 6
        const val SUNDAY = 7
    }
}