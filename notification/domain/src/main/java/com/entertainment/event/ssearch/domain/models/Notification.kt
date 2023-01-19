package com.entertainment.event.ssearch.domain.models

data class Notification(
    val notificationId: Int,
    val appPackageName: String,
    val time: Long,
    val title: String,
    val body: String,
)