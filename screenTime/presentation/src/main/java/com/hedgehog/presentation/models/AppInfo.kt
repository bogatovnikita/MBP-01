package com.hedgehog.presentation.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val nameApp: String = "",
    val icon: Drawable? = null,
    val listTime: MutableList<Int> = mutableListOf(),
    val lastLaunch: String = "",
    val data: String = "",
    val totalTimeUsage: String = ""
)