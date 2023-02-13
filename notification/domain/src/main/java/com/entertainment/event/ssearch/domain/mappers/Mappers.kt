package com.entertainment.event.ssearch.domain.mappers

import android.content.Context
import android.content.pm.PackageInfo
import android.net.Uri
import com.entertainment.event.ssearch.domain.models.App
import com.entertainment.event.ssearch.domain.models.AppWithNotifications

fun List<PackageInfo>.mapToApp(context: Context) = this.map { packageInfo ->
    packageInfo.mapToApp(context)
}

fun PackageInfo.mapToApp(context: Context) =
    App(
        packageName = packageName,
        icon = Uri.parse("android.resource://" + packageName + "/" + applicationInfo.icon).toString(),
        name = context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(packageName, 0)).toString(),
        isSwitched = true
    )

fun List<App>.mapToAppWithEmptyNotifications() = this.map { app ->
    app.mapToAppWithEmptyNotifications()
}

fun App.mapToAppWithEmptyNotifications() =
    AppWithNotifications(
        app = this,
        listNotifications = emptyList()
    )