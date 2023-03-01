package com.entertainment.event.ssearch.data.general_device_info

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build.*
import android.os.Build.VERSION.RELEASE
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import com.entertainment.event.ssearch.data.util.Util
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import general.R
import java.util.*
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class GeneralDeviceInfoImpl @Inject constructor(
    private val context: Application,
) : Util(context), GeneralDeviceInfo {

    private val deviceModel
        get() = capitalize(
            if (MODEL.lowercase(Locale.getDefault())
                    .startsWith(MANUFACTURER.lowercase(Locale.getDefault()))
            ) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            }
        )

    private val osVersion: String
        get() = RELEASE

    private val time: String
        get() {
            return formatTime(SystemClock.uptimeMillis())
        }

    private val hardware: String? = HARDWARE

    private val ppi: String
        get() {
            val diagonal = screenDiagonal
            val screenHeightPx = displayHeight
            val screenWidthPx = displayWidth
            val PPI =
                sqrt((screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx).toDouble())
                    .toFloat() / diagonal
            return round(PPI, 2).toString()
        }

    private val screenDiagonal: Double
        get() {
            val dm = Resources.getSystem().displayMetrics
            val width = displayWidth
            val height = displayHeight
            val wi = width.toDouble() / dm.xdpi.toDouble()
            val hi = height.toDouble() / dm.ydpi.toDouble()
            val x = wi.pow(2.0)
            val y = hi.pow(2.0)
            val screenInches = round(sqrt(x + y), 1)
            return screenInches
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

    override fun getGeneralDeviceInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = getString(R.string.general_info), id = 9),
        listFun = listOf(
            ChildFun(name = getString(R.string.model), body = deviceModel, id = 10),
            ChildFun(name = getString(R.string.android), body = osVersion, id = 11),
            ChildFun(name = getString(R.string.work_time), body = time, id = 12),
            ChildFun(name = getString(R.string.motherboard), body = hardware ?: "none", id = 13),
            ChildFun(
                name = getString(R.string.display),
                body = "$screenDiagonal ${getString(R.string.`in`)}",
                id = 14
            ),
            ChildFun(
                name = getString(R.string.screen_resolution),
                body = "$displayHeight x $displayWidth ${getString(R.string.pi)}",
                id = 15
            ),
            ChildFun(name = getString(R.string.density), body = "$ppi ppi", id = 16),
        )
    )

}