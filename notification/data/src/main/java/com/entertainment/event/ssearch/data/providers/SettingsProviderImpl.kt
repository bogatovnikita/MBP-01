package com.entertainment.event.ssearch.data.providers

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.entertainment.event.ssearch.domain.providers.SettingsProvider
import javax.inject.Inject

class SettingsProviderImpl @Inject constructor(
    context: Application
) : SettingsProvider {

    private val prefs = context.getSharedPreferences(NOTIFICATION_SETTINGS, MODE_PRIVATE)

    override suspend fun switchOffDisturbMode(isSwitched: Boolean) =
        prefs.edit().putBoolean(DISTURB_MODE, isSwitched).apply()

    override suspend fun isDisturbModeSwitched(): Boolean = prefs.getBoolean(DISTURB_MODE, false)

    override suspend fun switchLimitAllApps(isSwitched: Boolean) =
        prefs.edit().putBoolean(LIMIT_ALL_APP, isSwitched).apply()

    override suspend fun isAllAppsLimited(): Boolean = prefs.getBoolean(LIMIT_ALL_APP, false)

    companion object {
        const val NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS"
        const val DISTURB_MODE = "DISTURB_MODE"
        const val LIMIT_ALL_APP = "LIMIT_ALL_APP"
    }

}