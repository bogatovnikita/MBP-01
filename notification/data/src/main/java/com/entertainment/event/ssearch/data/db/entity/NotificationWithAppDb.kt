package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

class NotificationWithAppDb(
    @Embedded
    val notificationDb: NotificationDb,
    @Relation(
        parentColumn = "appPackageName",
        entityColumn = "packageName"
    )
    val app: AppDb
)