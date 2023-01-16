package com.entertainment.event.ssearch.data.repositories

import android.app.Notification
import android.service.notification.StatusBarNotification
import com.entertainment.event.ssearch.data.db.entity.App
import com.entertainment.event.ssearch.data.db.entity.AppWithNotifications
import com.entertainment.event.ssearch.domain.models.AppDomain
import com.entertainment.event.ssearch.domain.models.AppWithNotificationsDomain
import com.entertainment.event.ssearch.domain.models.NotificationDomain


fun List<com.entertainment.event.ssearch.data.db.entity.Notification>.mapToNotificationDomain() =
    this.map { notification ->
        NotificationDomain(
            notificationId = notification.notificationId,
            appPackageName = notification.appPackageName,
            time = notification.time,
            title = notification.title,
            body = notification.body,
        )
    }

fun List<AppWithNotifications>.mapToAppDomain() = this.map { appWithNotifications ->
    AppWithNotificationsDomain(
        packageName = appWithNotifications.app.packageName,
        listNotifications = appWithNotifications.notifications.mapToNotificationDomain(),
        isSwitched = appWithNotifications.app.isSwitched
    )
}

fun StatusBarNotification.mapToNotification() =
    com.entertainment.event.ssearch.data.db.entity.Notification(
        appPackageName = packageName,
        time = postTime,
        title = notification.extras[Notification.EXTRA_TITLE].toString(),
        body = notification.extras[Notification.EXTRA_TEXT].toString()
    )

fun AppDomain.mapToApp() = App(
    packageName = packageName,
    isSwitched = isSwitched
)

fun App.mapToAppDomain() = AppDomain(
    packageName = packageName,
    isSwitched = isSwitched
)