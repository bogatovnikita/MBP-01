package com.entertainment.event.ssearch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsWithAppDao
import com.entertainment.event.ssearch.data.db.entity.AppDb
import com.entertainment.event.ssearch.data.db.entity.NotificationDb



@Database(entities = [
    NotificationDb::class,
    AppDb::class,
 ], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationsDao(): NotificationsDao

    abstract fun appDao(): AppDao

    abstract fun appWithNotificationsDao(): AppWithNotificationsDao

    abstract fun notificationsWithAppDao(): NotificationsWithAppDao

    companion object {

        const val DB_NOTIFICATION_NAME = "notification_database"

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