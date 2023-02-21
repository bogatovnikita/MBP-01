package com.entertainment.event.ssearch.data.general_device_info

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Build.*
import android.os.Build.VERSION.RELEASE
import android.os.Build.VERSION.SDK_INT
import android.util.DisplayMetrics
import android.view.WindowManager
import java.util.*
import javax.inject.Inject

class GeneralDeviceInfoImpl @Inject constructor(
    private val context: Application,
) {

    val model = deviceModel

    val hardware: String? = HARDWARE

    val board: String? = BOARD

    val bootloader: String? = BOOTLOADER

    val user: String? = USER

    val host: String? = HOST

    val version: String? = RELEASE

    val apiLevel = SDK_INT

    val id: String? = ID

    val time = TIME

    val fingerPrint: String? = FINGERPRINT

    val display: String? = DISPLAY

    val osVersion: String
        get() = Build.VERSION.RELEASE

    // deprecated
    val screenHeight: Int
        get() {
            var height = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            height = display.height
            return height
        }

    // deprecated
    val screenWidth: Int
        get() {
            var width = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = if (SDK_INT >= VERSION_CODES.R) context.display else wm.defaultDisplay
                    width = display?.width ?: 0
            return width
        }

    private val deviceModel
        @SuppressLint("DefaultLocale")
        get() = capitalize(
            if (MODEL.lowercase(Locale.getDefault())
                    .startsWith(MANUFACTURER.lowercase(Locale.getDefault()))
            ) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            }
        )


    private fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) uppercaseChar() }
        }
    }

    fun getDisplayHeight(): Int {
        return context.resources.displayMetrics.heightPixels + getNavigationBarHeight()
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

    /**
     * To get the resolution of the device's  display's width
     * @return display width in pixels
     */
    fun getDisplayWidth(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getAndroidVersion(): Int = Build.VERSION.SDK_INT

    fun getDeviceName(): String {
        val manafacture = MANUFACTURER
        val model = MODEL
        return if (model.startsWith(manafacture)) {
            capitalize(model)
        } else {
            capitalize(manafacture)
        }
    }

}