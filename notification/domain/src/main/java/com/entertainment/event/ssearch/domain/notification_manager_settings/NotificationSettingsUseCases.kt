package com.entertainment.event.ssearch.domain.notification_manager_settings

import android.app.Application
import com.entertainment.event.ssearch.domain.mappers.mapToApp
import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.providers.Settings
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationSettingsUseCases @Inject constructor(
    private val settings: Settings,
    private val apps: AppRepository,
    private val context: Application,
    private val appsProvide: AppsProvider,
    private val appsWithNotifications: AppWithNotificationsRepository,
) {

    suspend fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) =
        apps.setSwitched(packageName, isSwitched)

    suspend fun getDisturbMode(): Boolean = settings.isDisturbModeSwitched()

    suspend fun isAllAppsLimited(): Boolean = settings.isAllAppsLimited()

    suspend fun setGeneralDisturbMode(isSwitched: Boolean) = settings.switchOffDisturbMode(isSwitched)

    suspend fun getAppsInfo(hasPermission: Boolean): Flow<List<AppWithNotifications>> {
        return if (hasPermission) {
            appsWithNotifications.readAppsWithNotifications()
        } else {
            getOnlyApps()
        }
    }

    suspend fun updateApps() {
        removeDeletedApps()
        addNewApps()
    }

    private suspend fun addNewApps() {
        val listApps = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()
        listApps.forEach { app ->
            if (apps.readApp(app.packageName) == null) {
                apps.insertApp(app.mapToApp(context))
            }
        }
    }

    private suspend fun removeDeletedApps() {
        val phoneApps = getPhoneApps()
        val savedApps = apps.getApps()
        if (savedApps.size > phoneApps.size) {
            savedApps.forEach { app ->
                val isContain = phoneApps.find { packageInfo ->
                    packageInfo.packageName == app.packageName
                }
                if (isContain == null) {
                    apps.deleteApp(app.packageName)
                }
            }
        }

    }

    private suspend fun getOnlyApps(): Flow<List<AppWithNotifications>> {
        return if (apps.getApps().isEmpty()) {
            val phoneApps = getPhoneApps()
            apps.insertAll(phoneApps.mapToApp(context))
            appsWithNotifications.readAppsWithNotifications()
        } else {
            appsWithNotifications.readAppsWithNotifications()
        }
    }

    suspend fun switchModeDisturbForAllApps(isSwitched: Boolean) {
        settings.switchLimitAllApps(isSwitched)
        apps.getApps().forEach { app ->
            apps.setSwitched(app.packageName, isSwitched)
        }
    }

    private suspend fun getPhoneApps() = appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()

}