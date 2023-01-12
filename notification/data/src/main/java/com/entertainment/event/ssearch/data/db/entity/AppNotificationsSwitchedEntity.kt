package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppNotificationsSwitchedEntity(
    @PrimaryKey
    val packageName: String,
    val isSwitched: Boolean
)
