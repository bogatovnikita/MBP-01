package com.entertainment.event.ssearch.data.util

import android.app.Application
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

abstract class Util(private val context: Application) {

    protected fun getString(id: Int): String = context.getString(id)

    protected fun round(value: Double, precision: Int): Double {
        val scale = 10.0.pow(precision.toDouble()).toInt()
        return (value * scale).roundToInt().toDouble() / scale
    }

    protected fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) uppercaseChar() }
        }
    }

    protected fun Float.toF() = this * 1.8 + 32

    protected fun formatTime(millis: Long): String {
        var seconds = (millis.toDouble() / 1000).roundToLong()
        val hours = TimeUnit.SECONDS.toHours(seconds)
        if (hours > 0) seconds -= TimeUnit.HOURS.toSeconds(hours)
        val minutes = if (seconds > 0) TimeUnit.SECONDS.toMinutes(seconds) else 0
        if (minutes > 0) seconds -= TimeUnit.MINUTES.toSeconds(minutes)
        return if (hours > 0) String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        ) else String.format("%02d:%02d", minutes, seconds)
    }

}