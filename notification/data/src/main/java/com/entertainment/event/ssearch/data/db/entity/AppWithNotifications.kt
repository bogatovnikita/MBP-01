package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "app_with_notifications_table")
data class AppWithNotifications(
    @Embedded
    val app: App,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "appPackageName"
    )
    val notifications: List<Notification>
)