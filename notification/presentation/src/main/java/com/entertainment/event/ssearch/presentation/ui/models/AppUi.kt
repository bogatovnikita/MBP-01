package com.entertainment.event.ssearch.presentation.ui.models

data class AppUi(
    val packageName: String,
    val icon: String,
    val name: String,
    val countNotifications: Int,
    val lastNotificationTime: Long,
    val isSwitched: Boolean,
)
