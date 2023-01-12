package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["packageName", "body"])
data class NotificationEntity (
    val packageName: String,
    val time: Long,
    val title: String,
    val body: String,
)