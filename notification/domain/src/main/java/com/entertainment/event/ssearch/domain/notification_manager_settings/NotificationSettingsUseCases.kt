package com.entertainment.event.ssearch.domain.notification_manager_settings

import com.entertainment.event.ssearch.domain.mappers.mapToAppDomain
import com.entertainment.event.ssearch.domain.mappers.mapToAppWithEmptyNotifications
import com.entertainment.event.ssearch.domain.models.AppWithNotificationsDomain
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationSettingsUseCases @Inject constructor(
    private val appRepo: AppRepository,
    private val appsProvide: AppsProvider,
    private val appWithNotificationRepo: AppWithNotificationsRepository,
) {

    fun removeAllNotification() {}

    fun switchModeNotDisturb() {}

    fun getInfoAboutNotDisturbMode() {}

    suspend fun updateApp(app: AppWithNotificationsDomain) {
        appRepo.setSwitched(app.packageName, app.isSwitched)
    }

    suspend fun getAppsInfo(hasPermission: Boolean): Flow<List<AppWithNotificationsDomain>> {
        return if (hasPermission) {
            appWithNotificationRepo.readAppsWithNotifications()
        } else {
            getOnlyApps()
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

    private suspend fun getOnlyApps(): Flow<List<AppWithNotificationsDomain>> {
        return if (appRepo.readAll().count() == 0) {
            val listApps = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()
            appRepo.insertAll( listApps.mapToAppDomain())
            appWithNotificationRepo.readAppsWithNotifications()
        } else {
            appRepo.readAll().map { apps -> apps.mapToAppWithEmptyNotifications() }
        }
    }

}