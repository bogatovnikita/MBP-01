package com.entertainment.event.ssearch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.entertainment.event.ssearch.data.db.dao.AppNotificationsSwitchedDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import com.entertainment.event.ssearch.data.db.entity.AppNotificationsSwitchedEntity
import com.entertainment.event.ssearch.data.db.entity.NotificationEntity


private const val DB_NOTIFICATION_NAME = "notification_database"

@Database(entities = [
    NotificationEntity::class,
    AppNotificationsSwitchedEntity::class,
 ], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationsDao(): NotificationsDao

    abstract fun appNotificationSwitchedDao(): AppNotificationsSwitchedDao

    companion object {

        private var notificationDatabase: NotificationDatabase? = null

        fun create(context: Context): NotificationDatabase {

            return if (notificationDatabase == null) {
                notificationDatabase = Room.databaseBuilder(
                    context,
                    NotificationDatabase::class.java,
                    DB_NOTIFICATION_NAME
                ).build()
                notificationDatabase!!
            } else {
                notificationDatabase!!
            }

        }
    }

}