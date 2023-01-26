package com.entertainment.event.ssearch.presentation.mappers

import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.models.Notification
import com.entertainment.event.ssearch.domain.models.NotificationWithApp
import com.entertainment.event.ssearch.presentation.models.AppUi
import com.entertainment.event.ssearch.presentation.models.NotificationUi


fun List<AppWithNotifications>.mapToAppUiList(hasPermission: Boolean) = this.map { app ->
    app.mapToAppUi(hasPermission)
}

private fun AppWithNotifications.mapToAppUi(hasPermission: Boolean) = AppUi(
    icon =  app.icon,
    name =  app.name,
    isSwitched =  app.isSwitched,
    packageName = app.packageName,
    hasPermission = hasPermission,
    countNotifications = listNotifications.size,
)

fun List<NotificationWithApp>.toNotificationUi() = this.map { notificationWithApp ->
    notificationWithApp.toNotificationUi()
}

fun NotificationWithApp.toNotificationUi() = NotificationUi(
    packageName = packageName,
    title = title,
    name = name,
    icon = icon,
    body = body,
    time = time,
)

fun NotificationUi.toNotification() = Notification(
    appPackageName = packageName,
    title = title,
    body = body,
    time = time,
)