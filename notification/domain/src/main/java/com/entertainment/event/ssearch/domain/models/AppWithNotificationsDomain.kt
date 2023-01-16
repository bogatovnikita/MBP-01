package com.entertainment.event.ssearch.domain.models

data class AppWithNotificationsDomain(
    val packageName: String,
    val listNotifications: List<NotificationDomain>,
    val isSwitched: Boolean,
)