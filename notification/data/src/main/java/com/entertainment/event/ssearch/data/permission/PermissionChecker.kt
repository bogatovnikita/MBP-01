package com.entertainment.event.ssearch.data.permission

import android.Manifest
import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.entertainment.event.ssearch.data.background.NotificationService
import com.entertainment.event.ssearch.domain.permission.Permission
import javax.inject.Inject

class PermissionChecker @Inject constructor(
    private val context: Application
): Permission {

    override fun hasServicePermission(): Boolean {
        val string = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: ""
        val listenersClassNames = string.split(":")
        val listenerName =
            ComponentName(context, NotificationService::class.java).flattenToString()
        return listenersClassNames.contains(listenerName)
    }

    @RequiresApi(33)
    override fun hasNotificationPermission(): Boolean = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

}