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

    companion object {
        const val NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS"
        const val DISTURB_MODE = "DISTURB_MODE"
    }

}