package com.entertainment.event.ssearch.domain.mappers

import android.content.pm.PackageInfo
import com.entertainment.event.ssearch.domain.models.AppDomain

fun List<PackageInfo>.mapToAppDomain() = this.map { packageInfo ->
    packageInfo.mapToAppDomain()
}

fun PackageInfo.mapToAppDomain() =
    AppDomain(
        packageName = packageName,
        listNotifications = emptyList(),
        isSwitched = false
    )