package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.content.Context
import com.entertainment.event.ssearch.data.dnd.DNDSettingsImpl.Companion.NOTIFICATION_SETTINGS
import com.entertainment.event.ssearch.domain.dnd.TimePickerSettings
import javax.inject.Inject

class TimePickerSettingsImpl @Inject constructor(
    context: Application
) : TimePickerSettings {

    private val prefs = context.getSharedPreferences(
        NOTIFICATION_SETTINGS,
        Context.MODE_PRIVATE
    )

    override suspend fun getStartHours(): Int = prefs.getInt(START_HOURS, 0)

    override suspend fun setStartHours(hours: Int) = prefs.edit().putInt(START_HOURS, hours).apply()

    override suspend fun getStartMinutes(): Int = prefs.getInt(START_MINUTES, 0)

    override suspend fun setStartMinutes(minutes: Int) =
        prefs.edit().putInt(START_MINUTES, minutes).apply()

    override suspend fun setMondayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(MONDAY, isSwitched).apply()

    override suspend fun isMondayIncluded(): Boolean = prefs.getBoolean(MONDAY, false)

    override suspend fun setTuesdayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(TUESDAY, isSwitched).apply()

    override suspend fun isTuesdayIncluded(): Boolean = prefs.getBoolean(TUESDAY, false)

    override suspend fun setWednesdayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(WEDNESDAY, isSwitched).apply()

    override suspend fun isWednesdayIncluded(): Boolean = prefs.getBoolean(WEDNESDAY, false)

    override suspend fun setThursdayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(THURSDAY, isSwitched).apply()

    override suspend fun isThursdayIncluded(): Boolean = prefs.getBoolean(THURSDAY, false)

    override suspend fun setFridayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(FRIDAY, isSwitched).apply()

    override suspend fun isFridayIncluded(): Boolean = prefs.getBoolean(FRIDAY, false)

    override suspend fun setSaturdayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(SATURDAY, isSwitched).apply()

    override suspend fun isSaturdayIncluded(): Boolean = prefs.getBoolean(SATURDAY, false)

    override suspend fun setSundayInclude(isSwitched: Boolean) =
        prefs.edit().putBoolean(SUNDAY, isSwitched).apply()

    override suspend fun isSundayIncluded(): Boolean = prefs.getBoolean(SUNDAY, false)

    companion object {
        const val START_HOURS = "START_HOURS"
        const val START_MINUTES = "START_MINUTES"
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
    }

}