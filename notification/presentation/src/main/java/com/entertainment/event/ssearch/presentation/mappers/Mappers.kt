package com.entertainment.event.ssearch.presentation.mappers

import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.presentation.models.AppUi


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
