package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.app.NotificationManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.entertainment.event.ssearch.domain.dnd.DNDController
import javax.inject.Inject

class DNDControllerImpl @Inject constructor(
    private val context: Application,
) : DNDController {

    private val notificationManager = getNotificationManager()

    override suspend fun setDNDModeOn() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
    }

    override suspend  fun setDNDModeOff() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }

    override suspend fun isDNDModeOff(): Boolean =
        Settings.Global.getInt(context.contentResolver, "zen_mode") == NotificationManager.INTERRUPTION_FILTER_UNKNOWN

    private fun getNotificationManager(): NotificationManager =
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

}