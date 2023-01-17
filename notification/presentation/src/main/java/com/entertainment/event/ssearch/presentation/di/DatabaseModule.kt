package com.entertainment.event.ssearch.presentation.di

import android.app.Application
import androidx.room.Room
import com.entertainment.event.ssearch.data.db.DB_NOTIFICATION_NAME
import com.entertainment.event.ssearch.data.db.NotificationDatabase
import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideNotificationDatabase(context: Application): NotificationDatabase =
        Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            DB_NOTIFICATION_NAME
        ).build()

    @Provides
    fun provideNotificationsDao(db: NotificationDatabase): NotificationsDao = db.notificationsDao()

    @Provides
    fun provideAppNotificationSwitchedDao(db: NotificationDatabase): AppDao = db.appDao()

    @Provides
    fun provideAppWithNotificationsDao(db: NotificationDatabase): AppWithNotificationsDao =
        db.appWithNotificationsDao()

}