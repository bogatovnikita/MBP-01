package com.entertainment.event.ssearch.data.device_battery_info

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.BatteryManager.*
import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import javax.inject.Inject


class BatteryInfoImpl @Inject constructor(
    private val context: Application
) : BatteryInfo {

    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    private val batteryPercent: Int
        get() {
            val intent = batteryStatusIntent
            val rawlevel = intent!!.getIntExtra(EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(EXTRA_SCALE, -1)
            var level = -1
            if (rawlevel >= 0 && scale > 0) {
                level = rawlevel * 100 / scale
            }
            return level
        }


    private val currentNow: Long
        get() {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            return batteryManager.getLongProperty(BATTERY_PROPERTY_CURRENT_NOW)
        }

    private val avgCurrent: Long
        get() {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            return batteryManager.getLongProperty(BATTERY_PROPERTY_CURRENT_AVERAGE)
        }

    private val isPhoneCharging: Boolean
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(EXTRA_PLUGGED, 0)
            return plugged == BATTERY_PLUGGED_AC || plugged == BATTERY_PLUGGED_USB
        }

    private val batteryHealth: String
        get() {
            var health = BATTERY_HEALTH_UNKNOWN
            val intent = batteryStatusIntent
            val status = intent!!.getIntExtra(EXTRA_HEALTH, 0)
            when (status) {
                BATTERY_HEALTH_COLD -> health = COLD

                BATTERY_HEALTH_DEAD -> health = DEAD

                BATTERY_HEALTH_GOOD -> health = GOOD

                BATTERY_HEALTH_OVERHEAT -> health = OVERHEAT

                BATTERY_HEALTH_OVER_VOLTAGE -> health = OVER_VOLTAGE

                BatteryManager.BATTERY_HEALTH_UNKNOWN -> health = BATTERY_HEALTH_UNKNOWN

                BATTERY_HEALTH_UNSPECIFIED_FAILURE -> health = FAILURE
            }
            return health
        }

    private val batteryTechnology: String
        get() {
            val intent = batteryStatusIntent
            return intent!!.getStringExtra(EXTRA_TECHNOLOGY)!!
        }

    private val batteryTemperature: Float
        get() {
            val intent = batteryStatusIntent
            val temperature = intent!!.getIntExtra(EXTRA_TEMPERATURE, 0)
            return (temperature / 10.0).toFloat()
        }

    private val batteryVoltage: Int
        get() {
            val intent = batteryStatusIntent
            return intent!!.getIntExtra(EXTRA_VOLTAGE, 0)
        }

    private val chargingSource: String
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(EXTRA_PLUGGED, 0)
            when (plugged) {
                BATTERY_PLUGGED_AC -> return AC
                BATTERY_PLUGGED_USB -> return USB
                BATTERY_PLUGGED_WIRELESS -> return WIRELESS
                else -> return UNKNOWN
            }
        }

    private val batteryCapacity: Long
        get() {
            val mBatteryManager =
                context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val chargeCounter = mBatteryManager.getIntProperty(BATTERY_PROPERTY_CHARGE_COUNTER)
            val capacity = mBatteryManager.getIntProperty(BATTERY_PROPERTY_CAPACITY)
            return if (chargeCounter == Int.MIN_VALUE || capacity == Int.MIN_VALUE) 0 else (chargeCounter / capacity * 100).toLong()
        }

    private fun getBatteryCapacity(): Double {
        val powerProfile: Any
        var batteryCapacity = 0.0
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"
        try {
            powerProfile = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java)
                .newInstance(context)
            batteryCapacity = Class.forName(POWER_PROFILE_CLASS)
                .getMethod("getBatteryCapacity")
                .invoke(powerProfile) as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return batteryCapacity
    }


    companion object {

        private const val AC = "AC"
        private const val USB = "USB"
        private const val WIRELESS = "Wireless"
        private const val UNKNOWN = "Battery"

        private const val COLD = "cold"
        private const val DEAD = "dead"
        private const val GOOD = "good"
        private const val OVERHEAT = "Over Heat"
        private const val OVER_VOLTAGE = "Over Voltage"
        private const val BATTERY_HEALTH_UNKNOWN = "Unknown"
        private const val FAILURE = "Unspecified failure"
    }

    override suspend fun getBatteryDeviceInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = general.R.string.battery, id = 0),
        listFun = listOf(
            ChildFun(name = general.R.string.charge_level, body = "$batteryPercent %", id = 1),
            ChildFun(name = general.R.string.temperature, body = "$batteryTemperature ℃", id = 2),
            ChildFun(name = general.R.string.voltage, body = "$batteryVoltage mV", id = 3),
            ChildFun(name = general.R.string.current_measurement, body = "$avgCurrent mA", id = 4),
            ChildFun(name = general.R.string.battery_capacity, body = "$batteryCapacity", id = 5),
            ChildFun(name = general.R.string.technologies, body = batteryTechnology, id = 6),
            ChildFun(name = general.R.string.battery_status, body = batteryHealth, id = 7),
            ChildFun(name = general.R.string.power_supply, body = chargingSource, id = 8),
        ))
}