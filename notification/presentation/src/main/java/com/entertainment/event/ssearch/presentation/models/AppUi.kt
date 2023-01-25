package com.entertainment.event.ssearch.presentation.models

data class AppUi(
    val icon: String,
    val name: String,
    val isSwitched: Boolean,
    val packageName: String,
    val hasPermission: Boolean,
    val countNotifications: Int,
)
