package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppWithNotifications(
    @Embedded
    val app: App,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "appPackageName"
    )
    val notifications: List<Notification>
)