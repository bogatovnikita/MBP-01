package com.entertainment.event.ssearch.domain.mappers

import android.content.pm.PackageInfo
import com.entertainment.event.ssearch.domain.models.AppDomain
import com.entertainment.event.ssearch.domain.models.AppWithNotificationsDomain

fun List<PackageInfo>.mapToAppDomain() = this.map { packageInfo ->
    packageInfo.mapToAppDomain()
}

fun PackageInfo.mapToAppDomain() =
    AppDomain(
        packageName = packageName,
        isSwitched = false
    )

fun List<AppDomain>.mapToAppWithEmptyNotifications() = this.map { app ->
    app.mapToAppWithEmptyNotifications()
}

fun AppDomain.mapToAppWithEmptyNotifications() =
    AppWithNotificationsDomain(
        packageName = packageName,
        isSwitched = false,
        listNotifications = emptyList()
    )