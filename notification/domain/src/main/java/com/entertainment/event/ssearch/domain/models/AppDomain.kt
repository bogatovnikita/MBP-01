package com.entertainment.event.ssearch.domain.models

data class AppDomain(
    val packageName: String,
    val listNotifications: List<NotificationDomain>,
    val isSwitched: Boolean,
)