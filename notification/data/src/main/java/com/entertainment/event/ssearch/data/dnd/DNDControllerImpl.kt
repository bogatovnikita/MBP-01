package com.entertainment.event.ssearch.data.dnd

import android.app.Application
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class DNDControllerImpl @Inject constructor(
    private val context: Application,
) {

    private val notificationManager = getNotificationManager()

    private fun setDNDModeOn() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
    }

    private fun setDNDModeOff() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
    }

    private fun getNotificationManager(): NotificationManager =
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

}