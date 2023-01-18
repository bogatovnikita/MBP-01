package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppWithNotificationsDb(
    @Embedded
    val appDb: AppDb,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "appPackageName"
    )
    val notificationDbs: List<NotificationDb>
)