package com.entertainment.event.ssearch.data.db.entity

import android.app.Notification
import android.service.notification.StatusBarNotification
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class Notification (
    @PrimaryKey(autoGenerate = true)
    val notificationId: Int = 0,
    val appPackageName: String,
    val time: Long,
    val title: String,
    val body: String,
)

fun StatusBarNotification.mapToNotification() = Notification(
    appPackageName = packageName,
    time = postTime,
    title = notification.extras[Notification.EXTRA_TITLE].toString(),
    body = notification.extras[Notification.EXTRA_TEXT].toString()
)