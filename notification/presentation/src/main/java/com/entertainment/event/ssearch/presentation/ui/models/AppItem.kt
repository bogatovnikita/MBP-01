package com.entertainment.event.ssearch.presentation.ui.models

data class AppItem(
    val name: String,
    val icon: Int,
    val packageName: String,
    val countNotifications: Int,
    val isSwitched: Boolean,
)
