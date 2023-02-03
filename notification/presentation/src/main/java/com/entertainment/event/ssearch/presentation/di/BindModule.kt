package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.background.NotificationServiceController
import com.entertainment.event.ssearch.data.dnd.DNDControllerImpl
import com.entertainment.event.ssearch.data.permission.PermissionChecker
import com.entertainment.event.ssearch.data.providers.AppsProviderImpl
import com.entertainment.event.ssearch.data.dnd.DNDSettingsImpl
import com.entertainment.event.ssearch.data.dnd.DayPickerSettingsImpl
import com.entertainment.event.ssearch.data.dnd.TimeSettingsImpl
import com.entertainment.event.ssearch.data.repositories.Apps
import com.entertainment.event.ssearch.data.repositories.AppsWithNotifications
import com.entertainment.event.ssearch.data.repositories.Notifications
import com.entertainment.event.ssearch.data.repositories.NotificationsWithApp
import com.entertainment.event.ssearch.domain.dnd.DNDController
import com.entertainment.event.ssearch.domain.permission.Permission
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.dnd.DNDSettings
import com.entertainment.event.ssearch.domain.dnd.DayPickerSettings
import com.entertainment.event.ssearch.domain.dnd.TimeSettings
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import com.entertainment.event.ssearch.domain.repositories.NotificationsWithAppRepository
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
    fun bindNotificationsWithAppToNotificationsWithAppRepository(notifications: NotificationsWithApp): NotificationsWithAppRepository

    @Binds
    fun bindAppsProviderToAppsProviderImpl(apps: AppsProviderImpl): AppsProvider

    @Binds
    fun bindSettingsProviderToSettingsProviderImpl(settings: DNDSettingsImpl): DNDSettings

    @Binds
    fun bindTimeSettingsToTimeSettingsImpl(settings: TimeSettingsImpl): TimeSettings

    @Binds
    fun bindTimePickerSettingsToTimePickerSettingsImpl(settings: DayPickerSettingsImpl): DayPickerSettings

    @Binds
    fun bindPermissionCheckerToPermission(permission: PermissionChecker): Permission

    @Binds
    fun bindNotificationServiceControllerToServiceController(controller: NotificationServiceController): ServiceController

    @Binds
    fun bindDNDControllerImplToDNDController(controller: DNDControllerImpl): DNDController

}