package com.entertainment.event.ssearch.domain.notification_manager_settings

import android.app.Application
import com.entertainment.event.ssearch.domain.mappers.mapToApp
import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.providers.SettingsProvider
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationSettingsUseCases @Inject constructor(
    private val context: Application,
    private val appRepo: AppRepository,
    private val appsProvide: AppsProvider,
    private val settings: SettingsProvider,
    private val appWithNotificationRepo: AppWithNotificationsRepository,
) {

    suspend fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) =
        appRepo.setSwitched(packageName, isSwitched)

    suspend fun getDisturbMode(): Boolean = settings.isDisturbModeSwitched()

    suspend fun isAllAppsLimited(): Boolean = settings.isAllAppsLimited()

    suspend fun setGeneralDisturbMode(isSwitched: Boolean) = settings.switchOffDisturbMode(isSwitched)

    suspend fun getAppsInfo(hasPermission: Boolean): Flow<List<AppWithNotifications>> {
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
                    if (savedApp.app.packageName != newApp.packageName) {
                        appRepo.insertApp(newApp.mapToApp(context))
                    }
                }
            }
        }
    }

    private suspend fun getOnlyApps(): Flow<List<AppWithNotifications>> {
        val listApps = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()
        appRepo.insertAll(listApps.mapToApp(context))
        return appWithNotificationRepo.readAppsWithNotifications()
    }

    suspend fun switchModeDisturbForAllApps(isSwitched: Boolean) {
        settings.switchLimitAllApps(isSwitched)
        appRepo.getApps().forEach { app ->
            appRepo.setSwitched(app.packageName, isSwitched)
        }
    }

}