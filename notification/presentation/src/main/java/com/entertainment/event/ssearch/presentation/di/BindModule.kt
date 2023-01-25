package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.background.NotificationServiceController
import com.entertainment.event.ssearch.data.permission.PermissionChecker
import com.entertainment.event.ssearch.data.providers.AppsProviderImpl
import com.entertainment.event.ssearch.data.providers.SettingsImpl
import com.entertainment.event.ssearch.data.repositories.Apps
import com.entertainment.event.ssearch.data.repositories.AppsWithNotifications
import com.entertainment.event.ssearch.data.repositories.Notifications
import com.entertainment.event.ssearch.domain.permission.Permission
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.providers.Settings
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import com.entertainment.event.ssearch.domain.service.ServiceController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindAppRepositoryToAppRepositoryImpl(appsDb: Apps): AppRepository

    @Binds
    fun bindAppWithNotificationsRepoToAppWithNotificationsRepoImpl(appWithNotifications: AppsWithNotifications): AppWithNotificationsRepository

    @Binds
    fun bindNotificationRepositoryToNotificationRepositoryImpl(notifications: Notifications): NotificationRepository

    @Binds
    fun bindAppsProviderToAppsProviderImpl(apps: AppsProviderImpl): AppsProvider

    @Binds
    fun bindSettingsProviderToSettingsProviderImpl(settings: SettingsImpl): Settings

    @Binds
    fun bindPermissionCheckerToPermission(permission: PermissionChecker): Permission

    @Binds
    fun bindNotificationServiceControllerToServiceController(controller: NotificationServiceController): ServiceController

}