package com.battery_saving.presentation.utils

import android.app.ActivityManager
import android.content.Context

class KillBackgroundProcesses(private val context: Context) {

    fun killAllProcesses() {
        val list = context.packageManager.getInstalledApplications(0)
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        list.forEach {
            activityManager.killBackgroundProcesses(it.packageName)
        }
    }

}