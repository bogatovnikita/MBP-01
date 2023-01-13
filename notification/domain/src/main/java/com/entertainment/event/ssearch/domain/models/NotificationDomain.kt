package com.entertainment.event.ssearch.domain.models

data class NotificationDomain(
    val notificationId: Int,
    val appPackageName: String,
    val time: Long,
    val title: String,
    val body: String,
)