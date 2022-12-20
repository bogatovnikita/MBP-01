package com.hedgehog.presentation.extensions

import android.content.res.Resources
import com.hedgehog.presentation.R

fun Long.mapToAppTime(): String {
    val hours = (this / (1000 * 60 * 60)) % 24
    val minutes = (this / (1000 * 60)) % 60
    return Resources.getSystem().getString(R.string.D_hour_D_minutes, hours, minutes)
}

