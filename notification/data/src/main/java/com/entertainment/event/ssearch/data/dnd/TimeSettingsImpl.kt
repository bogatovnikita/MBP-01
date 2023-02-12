package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.content.Context
import com.entertainment.event.ssearch.domain.dnd.TimeSettings
import javax.inject.Inject

class TimeSettingsImpl @Inject constructor(
    private val context: Application,
): TimeSettings {

    private val prefs = context.getSharedPreferences(
        DNDSettingsImpl.NOTIFICATION_SETTINGS,
        Context.MODE_PRIVATE
    )

    override suspend fun getStartTime(): Int = prefs.getInt(START_TIME, DEFAULT_START_TIME)

    override suspend fun setStartTime(time: Int) = prefs.edit().putInt(START_TIME, time).apply()

    override suspend fun getEndTime(): Int = prefs.getInt(END_TIME, DEFAULT_END_TIME)

    override suspend fun setEndTime(time: Int) =
        prefs.edit().putInt(END_TIME, time).apply()

    companion object {
        private const val START_TIME = "START_HOURS"
        private const val END_TIME = "START_MINUTES"
        private const val DEFAULT_START_TIME = 480
        private const val DEFAULT_END_TIME = 1380
    }

}