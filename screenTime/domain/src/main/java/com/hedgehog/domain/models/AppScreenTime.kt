package com.hedgehog.domain.models

import android.graphics.drawable.Drawable

data class AppScreenTime(
    val packageName: String = "",
    val name: String = "",
    val time: String = "",
    val totalTime: Long = 0L,
    val icon: Drawable? = null,
    val isItSystemApp: Boolean = false
)