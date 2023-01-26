package com.entertainment.event.ssearch.domain.use_cases

import android.app.Application
import com.entertainment.event.ssearch.domain.models.Notification
import com.entertainment.event.ssearch.domain.models.NotificationWithApp
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import com.entertainment.event.ssearch.domain.repositories.NotificationsWithAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MissedNotificationUseCase @Inject constructor(
    private val context: Application,
    private val notifications: NotificationRepository,
    private val notificationsWithApp: NotificationsWithAppRepository,
) {

    suspend fun readAll(): Flow<List<NotificationWithApp>> =
        notificationsWithApp.readNotificationsWithApp()
            .map { list -> list.sortedBy { notification -> notification.time }.reversed() }

    suspend fun delete(notification: Notification) {
        notifications.delete(notification)
    }

    suspend fun deleteAll() = notifications.deleteAll()

    fun startAppByPackageName(packageName: String) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(launchIntent)
    }

}