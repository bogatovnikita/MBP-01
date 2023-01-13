package com.entertainment.event.ssearch.domain.notification_manager_settings

import com.entertainment.event.ssearch.domain.models.AppDomain

class NotificationSettingsUseCases {

    fun removeAllNotification() {}

    fun getAppsInfo(hasPermission: Boolean): List<AppDomain> {
        return emptyList()
    }
}