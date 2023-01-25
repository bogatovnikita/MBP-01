package com.hedgehog.presentation.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val nameApp: String = "",
    val icon: Drawable? = null,
    val timeEveryHour: MutableList<Int> = mutableListOf(),
    val lastLaunch: String = "",
    val batteryCharge: Int = 0,
    val data: String = "",
    val totalTimeUsage: Int = 0
)