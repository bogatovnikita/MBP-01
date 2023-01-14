package com.entertainment.event.ssearch.domain.notification_manager_settings

import com.entertainment.event.ssearch.domain.mappers.mapToAppDomain
import com.entertainment.event.ssearch.domain.models.AppDomain
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationSettingsUseCases @Inject constructor(
    private val appRepo: AppRepository,
    private val appsProvide: AppsProvider,
    private val appWithNotificationRepo: AppWithNotificationsRepository,
) {

    fun removeAllNotification() {}

    fun switchModeNotDisturb() {}

    fun getInfoAboutNotDisturbMode() {}

    suspend fun updateApp(app: AppDomain) {
        appRepo.setSwitched(app.packageName, app.isSwitched)
    }

    suspend fun getAppsInfo(hasPermission: Boolean): Flow<List<AppDomain>> {
        return if (hasPermission) {
            appWithNotificationRepo.readAppsWithNotifications()
        } else {
            val listApps = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()
            appRepo.insertAll( listApps.mapToAppDomain())
            appWithNotificationRepo.readAppsWithNotifications()
        }
    }

    suspend fun updateApps() {
        val listApps = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()
        appWithNotificationRepo.readAppsWithNotifications().collect { savedApps ->
            savedApps.forEach { savedApp ->
                listApps.forEach { newApp ->
                    if (savedApp.packageName != newApp.packageName) {
                        appRepo.insertApp(newApp.mapToAppDomain())
                    }
                }
            }
        }
    }

}