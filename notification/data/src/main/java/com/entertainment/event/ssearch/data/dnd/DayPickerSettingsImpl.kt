package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.content.Context
import com.entertainment.event.ssearch.data.dnd.DNDSettingsImpl.Companion.NOTIFICATION_SETTINGS
import com.entertainment.event.ssearch.domain.dnd.DayPickerSettings
import javax.inject.Inject

class DayPickerSettingsImpl @Inject constructor(
    context: Application
) : DayPickerSettings {

    private val prefs = context.getSharedPreferences(
        NOTIFICATION_SETTINGS,
        Context.MODE_PRIVATE
    )

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
        private const val MONDAY = "MONDAY"
        private const val TUESDAY = "TUESDAY"
        private const val WEDNESDAY = "WEDNESDAY"
        private const val THURSDAY = "THURSDAY"
        private const val FRIDAY = "FRIDAY"
        private const val SATURDAY = "SATURDAY"
        private const val SUNDAY = "SUNDAY"
    }

}