package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId: Int = 0,
    val appPackageName: String,
    val time: Long,
    val title: String,
    val body: String,
)