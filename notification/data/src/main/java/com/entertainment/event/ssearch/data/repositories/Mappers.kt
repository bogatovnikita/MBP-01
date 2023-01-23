package com.entertainment.event.ssearch.data.repositories

import android.service.notification.StatusBarNotification
import com.entertainment.event.ssearch.data.db.entity.AppDb
import com.entertainment.event.ssearch.data.db.entity.AppWithNotificationsDb
import com.entertainment.event.ssearch.data.db.entity.NotificationDb
import com.entertainment.event.ssearch.domain.models.App
import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.models.Notification


fun List<NotificationDb>.mapToNotification() =
    this.map { notification ->
        Notification(
            appPackageName = notification.appPackageName,
            time = notification.time,
            title = notification.title,
            body = notification.body,
        )
    }

fun Notification.mapToNotification() = NotificationDb(
    appPackageName = appPackageName,
    time = time,
    title = title,
    body = body
)

fun List<AppWithNotificationsDb>.mapToAppAppWithNotifications() = this.map { appWithNotifications ->
    AppWithNotifications(
        app = appWithNotifications.appDb.mapToApp(),
        listNotifications = appWithNotifications.notificationDbs.mapToNotification(),
    )
}

fun StatusBarNotification.mapToNotification() =
    Notification(
        appPackageName = packageName,
        time = postTime,
        title = notification.extras[android.app.Notification.EXTRA_TITLE].toString(),
        body = notification.extras[android.app.Notification.EXTRA_TEXT].toString()
    )

fun App.mapToAppDb() = AppDb(
    packageName = packageName,
    icon = icon,
    name = name,
    isSwitched = isSwitched
)

fun AppDb.mapToApp() = App(
    packageName = packageName,
    icon = icon,
    name = name,
    isSwitched = isSwitched
)