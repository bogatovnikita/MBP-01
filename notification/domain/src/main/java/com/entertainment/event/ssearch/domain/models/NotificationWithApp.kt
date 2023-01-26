package com.entertainment.event.ssearch.domain.models

data class NotificationWithApp(
    val packageName: String,
    val title: String,
    val name: String,
    val icon: String,
    val body: String,
    val time: Long,
)
