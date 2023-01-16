package com.entertainment.event.ssearch.presentation.ui.mappers

import android.app.Application
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.entertainment.event.ssearch.domain.models.AppWithNotificationsDomain
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.ui.models.AppItem
import javax.inject.Inject

class AppMapper @Inject constructor(
    private val context: Application
) {

    fun mapToAppItemList(apps: List<AppWithNotificationsDomain>) = apps.map { app ->
        mapToAppItem(app)
    }

    private fun mapToAppItem(app: AppWithNotificationsDomain) = AppItem(
        name =getName(app.packageName),
        icon = getIcon(app.packageName),
        packageName = app.packageName,
        countNotifications = getCountNotificationStr(app.listNotifications.size),
        isSwitched = app.isSwitched
    )

    private fun getIcon(packageName: String): Drawable =
        context.packageManager.getApplicationIcon(packageName)

    private fun getName(packageName: String): String =
        context.packageManager.getApplicationLabel(
            context.packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).toString()

    private fun getCountNotificationStr(size: Int): String {
        return if (size == 0) {
            EMPTY_LIST
        } else {
            context.getString(R.string.notification_maneger_count_notifications, size.toString())
        }
    }

    companion object {
        const val EMPTY_LIST = "EMPTY_LIST"
    }

}