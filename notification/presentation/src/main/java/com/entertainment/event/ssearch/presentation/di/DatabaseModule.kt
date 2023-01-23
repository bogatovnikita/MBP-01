package com.entertainment.event.ssearch.presentation.di

import android.app.Application
import com.entertainment.event.ssearch.data.db.NotificationDatabase
import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNotificationDatabase(context: Application): NotificationDatabase =
        NotificationDatabase.create(context)

    @Provides
    fun provideNotificationsDao(db: NotificationDatabase): NotificationsDao = db.notificationsDao()

    @Provides
    fun provideAppNotificationSwitchedDao(db: NotificationDatabase): AppDao = db.appDao()

    @Provides
    fun provideAppWithNotificationsDao(db: NotificationDatabase): AppWithNotificationsDao =
        db.appWithNotificationsDao()

}