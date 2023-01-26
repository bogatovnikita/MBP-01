package com.entertainment.event.ssearch.presentation.models

data class NotificationUi(
    val packageName: String,
    val title: String,
    val name: String,
    val icon: String,
    val body: String,
    val time: Long,
)
