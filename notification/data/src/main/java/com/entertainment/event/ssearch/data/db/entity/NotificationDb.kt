package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["appPackageName", "time"], tableName = "notification_table")
data class NotificationDb(
    val appPackageName: String,
    val time: Long,
    val title: String,
    val body: String,
)