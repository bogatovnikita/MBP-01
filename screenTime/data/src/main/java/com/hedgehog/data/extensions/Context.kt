package com.hedgehog.data.extensions

import android.content.Context
import android.content.pm.ApplicationInfo

fun Context.isPackageExist(target: String): Boolean {
    return packageManager.getInstalledApplications(0)
        .find { info -> info.packageName == target } != null
}

fun Context.isCheckAppPackage(target: String): Boolean {
    return target != this.packageName
}

fun Context.getAppLabel(packageName: String) = try {
    this.packageManager.getApplicationInfo(packageName, 0)
        .loadLabel(this.packageManager)
} catch (e: Exception) {
    packageName
}

fun Context.getAppIcon(packageName: String) = try {
    this.packageManager.getApplicationIcon(packageName)
} catch (e: Exception) {
    null
}

fun Context.checkIsSystemApp(packageName: String) = try {
    this.packageManager.getApplicationInfo(
        packageName, 0
    ).flags and ApplicationInfo.FLAG_SYSTEM != 0 || this.packageManager.getApplicationInfo(
        packageName, 0
    ).flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
} catch (e: Exception) {
    true
}