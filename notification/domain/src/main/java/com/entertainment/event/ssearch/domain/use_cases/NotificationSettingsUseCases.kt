package com.entertainment.event.ssearch.domain.use_cases

import android.app.Application
import com.entertainment.event.ssearch.domain.dnd.DNDController
import com.entertainment.event.ssearch.domain.mappers.mapToApp
import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.permission.Permission
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import com.entertainment.event.ssearch.domain.dnd.DNDSettings
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import com.entertainment.event.ssearch.domain.service.ServiceController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationSettingsUseCases @Inject constructor(
    private val apps: AppRepository,
    private val context: Application,
    private val permission: Permission,
    private val dndSettings: DNDSettings,
    private val appsProvide: AppsProvider,
    private val dndController: DNDController,
    private val serviceController: ServiceController,
    private val appsWithNotifications: AppWithNotificationsRepository,
) {

    suspend fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) =
        apps.setNotificationDisabled(packageName, isSwitched)

    fun checkAllAppsLimited(): Flow<Boolean> = flow {
        getAppsInfo().collect { apps ->
            val isAllAppsLimited = apps.none { it.app.isSwitched }
            dndSettings.setLimitAllApps(!isAllAppsLimited)
            emit(isAllAppsLimited)
        }
    }

    suspend fun getDisturbMode(): Boolean = !dndController.isDNDModeOff()

    fun hasServicePermission() = permission.hasServicePermission()

    fun hasNotificationPermission() = permission.hasNotificationPermission()

    fun clearAllNotification() {
        serviceController.cleanAllNotification()
    }

    suspend fun isAllAppsLimited(): Boolean = dndSettings.isAllAppsLimited()

    suspend fun setGeneralDisturbMode(isSwitched: Boolean) {
        dndSettings.setDisturbMode(isSwitched)
        if (isSwitched) {
            dndController.setDNDModeOn()
        } else {
            dndController.setDNDModeOff()
        }
    }

    suspend fun getAppsInfo(): Flow<List<AppWithNotifications>> {
        return if (permission.hasServicePermission()) {
            appsWithNotifications.readAppsWithNotifications().map {
                it.sortedWith(
                    compareBy(
                        { !it.app.isSwitched },
                        { it.listNotifications.maxByOrNull { it.time }?.time ?: -1L })
                ).reversed()
            }
        } else {
            getOnlyApps()
        }
    }

    fun startServiceIfNeed() {
        serviceController.startServiceIfNeed()
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
        dndSettings.setLimitAllApps(isSwitched)
        val updatedApps = apps.getApps().map { app -> app.copy(isSwitched = isSwitched) }
        apps.updateAll(updatedApps)
    }

    private suspend fun getPhoneApps() =
        appsProvide.getSystemPackages() + appsProvide.getInstalledPackages()

}