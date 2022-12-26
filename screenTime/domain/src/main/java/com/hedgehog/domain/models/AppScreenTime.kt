package com.hedgehog.domain.models

import android.graphics.drawable.Drawable

data class AppScreenTime(
    val name: String = "",
    val time: String = "",
    val icon: Drawable? = null
)