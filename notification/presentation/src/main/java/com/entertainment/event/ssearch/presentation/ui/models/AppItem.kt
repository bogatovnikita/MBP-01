package com.entertainment.event.ssearch.presentation.ui.models

import android.graphics.drawable.Drawable

data class AppItem(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val countNotifications: String,
    val isSwitched: Boolean,
)
