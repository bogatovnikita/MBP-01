package com.entertainment.event.ssearch.presentation.ui.mappers

import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.presentation.ui.models.AppUi


fun List<AppWithNotifications>.mapToAppUiList() = this.map { app ->
    app.mapToAppUi()
}

private fun AppWithNotifications.mapToAppUi() = AppUi(
    packageName = app.packageName,
    icon =  app.icon,
    name =  app.name,
    countNotifications = listNotifications.size,
    lastNotificationTime = listNotifications.maxByOrNull { it.time }?.time ?: 0L,
    isSwitched =  app.isSwitched,
)
