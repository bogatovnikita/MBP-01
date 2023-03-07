package com.onboarding.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.BatteryManager
import android.os.Build
import android.os.Build.HARDWARE
import android.os.Build.VERSION.RELEASE
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import general.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

class GetAboutDeviceInfo(private val context: Context) {

    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    private val displayWidth: Int
        get() = context.resources.displayMetrics.widthPixels

    private val displayHeight: Int
        get() = screenHeight + getNavigationBarHeight()

    private val screenHeight: Int
        get() {
            val dm = Resources.getSystem().displayMetrics
            return dm.heightPixels
        }

    private fun getNavigationBarHeight(): Int {
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            return 0
        }
    }

    fun getBatteryTemperature(): String {
        val intent = batteryStatusIntent
        val temperature = intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val value = (temperature / 10.0).toFloat()
        return "$value ℃ (${kotlin.math.round(value.toF())} ℉)"
    }

    fun getBatteryChargePercent(): String {
        val percent = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val value = percent.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return "$value%"
    }

    fun getDeviceModel(): String {
        return capitalize(
            if (Build.MODEL.lowercase(Locale.getDefault())
                    .startsWith(Build.MANUFACTURER.lowercase(Locale.getDefault()))
            ) {
                Build.MODEL
            } else {
                "${Build.MANUFACTURER} ${Build.MODEL}"
            }
        )
    }

    private fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) uppercaseChar() }
        }
    }

    fun getAndroidVersion(): String = RELEASE

    fun getTimeWork(): String {
        return formatTime(SystemClock.uptimeMillis())
    }

    private fun formatTime(millis: Long): String {
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

    fun getCardVersion(): String {
        return HARDWARE ?: "none"
    }

    fun screenDiagonal(): String {
        val screenInches = getDiagonalSize()
        return "$screenInches ${context.getString(R.string.`in`)}"
    }

    private fun getDiagonalSize(): Double {
        val dm = Resources.getSystem().displayMetrics
        val width = displayWidth
        val height = displayHeight
        val wi = width.toDouble() / dm.xdpi.toDouble()
        val hi = height.toDouble() / dm.ydpi.toDouble()
        val x = wi.pow(2.0)
        val y = hi.pow(2.0)
        return round(sqrt(x + y), 1)
    }

    private fun round(value: Double, precision: Int): Double {
        val scale = 10.0.pow(precision.toDouble()).toInt()
        return (value * scale).roundToInt().toDouble() / scale
    }

    fun screenSize(): String = "$displayHeight x $displayWidth ${context.getString(R.string.pi)}"

    fun getPpi(): String {
        val diagonal = getDiagonalSize()
        val screenHeightPx = displayHeight
        val screenWidthPx = displayWidth
        val ppi =
            sqrt((screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx).toDouble())
                .toFloat() / diagonal
        return "${round(ppi, 2)} ppi"
    }

    private fun Float.toF() = this * 1.8 + 32
}

