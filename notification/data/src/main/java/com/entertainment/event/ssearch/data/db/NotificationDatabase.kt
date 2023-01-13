package com.entertainment.event.ssearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import com.entertainment.event.ssearch.data.db.entity.App
import com.entertainment.event.ssearch.data.db.entity.Notification


const val DB_NOTIFICATION_NAME = "notification_database"

@Database(entities = [
    Notification::class,
    App::class,
 ], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationsDao(): NotificationsDao

    abstract fun appDao(): AppDao

    abstract fun appWithNotificationsDao(): AppWithNotificationsDao

}