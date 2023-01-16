package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.providers.AppsProviderImpl
import com.entertainment.event.ssearch.data.repositories.AppRepositoryImpl
import com.entertainment.event.ssearch.data.repositories.AppWithNotificationsRepoImpl
import com.entertainment.event.ssearch.data.repositories.NotificationRepositoryImpl
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindAppRepositoryToAppRepositoryImpl(appRepositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    fun bindAppWithNotificationsRepoToAppWithNotificationsRepoImpl(appWithNotificationsRepoImpl: AppWithNotificationsRepoImpl): AppWithNotificationsRepository

    @Binds
    fun bindNotificationRepositoryToNotificationRepositoryImpl(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    fun bindAppsProviderToAppsProviderImpl(appsProviderImpl: AppsProviderImpl): AppsProvider

}