package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.entertainment.event.ssearch.domain.dnd.DNDSettings
import javax.inject.Inject

class DNDSettingsImpl @Inject constructor(
    context: Application
) : DNDSettings {

    private val prefs = context.getSharedPreferences(NOTIFICATION_SETTINGS, MODE_PRIVATE)

    override suspend fun setDisturbMode(isSwitched: Boolean) =
        prefs.edit().putBoolean(DISTURB_MODE, isSwitched).apply()

    override suspend fun isDisturbModeEnabled(): Boolean = prefs.getBoolean(DISTURB_MODE, false)

    override suspend fun setLimitAllApps(isSwitched: Boolean) =
        prefs.edit().putBoolean(LIMIT_ALL_APP, isSwitched).apply()

    override suspend fun isAllAppsLimited(): Boolean = prefs.getBoolean(LIMIT_ALL_APP, false)

    override suspend fun setAutoModeDND(isSwitched: Boolean) =
        prefs.edit().putBoolean(AUTO_MODE_DND, isSwitched).apply()

    override suspend fun isAutoModeDNDEnabled(): Boolean = prefs.getBoolean(AUTO_MODE_DND, false)

    companion object {
        const val NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS"
        const val DISTURB_MODE = "DISTURB_MODE"
        const val LIMIT_ALL_APP = "LIMIT_ALL_APP"
        const val AUTO_MODE_DND = "AUTO_MODE_DND"
    }

}