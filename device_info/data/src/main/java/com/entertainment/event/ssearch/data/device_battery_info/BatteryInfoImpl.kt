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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt


class BatteryInfoImpl @Inject constructor(
    private val context: Application,
    private val batteryChargeReceiver: BatteryChargeReceiver,
) : BatteryInfo {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _batteryDeviceInfo: MutableStateFlow<DeviceFunctionGroup> = MutableStateFlow(getBatteryDeviceInfo())
    override val batteryDeviceInfo = _batteryDeviceInfo.asStateFlow()

    init {
        initObserveBatPercent()
        initObserveCurrentAndVoltage()
    }

    override fun stopObserve() {
        coroutineScope.cancel()
    }

    override fun registerBatteryReceiver() {
        batteryChargeReceiver.registerReceiver(context)
    }

    override fun unregisterBatteryReceiver() {
        batteryChargeReceiver.unregisterReceiver(context)
    }

    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    private val batteryPercent: Int
        get() {
            return batteryChargeReceiver.batteryPercent.value
        }

    private fun initObserveBatPercent() {
        coroutineScope.launch {
            batteryChargeReceiver.batteryPercent.collect {
                _batteryDeviceInfo.value = getBatteryDeviceInfo()
            }
        }
    }

    private fun initObserveCurrentAndVoltage() {
        coroutineScope.launch {
            while (true) {
                delay(3000)
                _batteryDeviceInfo.value = getBatteryDeviceInfo()
            }
        }
    }

    private val avgCurrent: Long
        get() {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            return batteryManager.getLongProperty(BATTERY_PROPERTY_CURRENT_AVERAGE)
        }

    private val batteryHealth: String
        get() {
            var health = getString(general.R.string.unknown)
            val intent = batteryStatusIntent
            val status = intent!!.getIntExtra(EXTRA_HEALTH, 0)
            when (status) {
                BATTERY_HEALTH_COLD -> health = getString(general.R.string.cold)

                BATTERY_HEALTH_DEAD -> health = getString(general.R.string.bad)

                BATTERY_HEALTH_GOOD -> health = getString(general.R.string.good)

                BATTERY_HEALTH_OVERHEAT -> health = getString(general.R.string.over_heat)

                BATTERY_HEALTH_OVER_VOLTAGE -> health = getString(general.R.string.over_voltage)

                BATTERY_HEALTH_UNKNOWN -> health = getString(general.R.string.unknown)
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
                BATTERY_PLUGGED_AC -> return getString(general.R.string.ac)
                BATTERY_PLUGGED_USB -> return getString(general.R.string.usb)
                BATTERY_PLUGGED_WIRELESS -> return getString(general.R.string.wireless)
                else -> return getString(general.R.string.battery)            }
        }

    private val batteryCapacity: Int
        get() {
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
            return batteryCapacity.toInt()
        }

    private fun getString(id: Int) = context.getString(id)

    private fun getBatteryDeviceInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = general.R.string.battery, id = 0),
        listFun = listOf(
            ChildFun(name = general.R.string.charge_level, body = "$batteryPercent %", id = 1),
            ChildFun(name = general.R.string.temperature, body = "$batteryTemperature (${round(batteryTemperature.toF())} ℉)", id = 2),
            ChildFun(name = general.R.string.voltage, body = "$batteryVoltage mV", id = 3),
            ChildFun(name = general.R.string.current_measurement, body = "$avgCurrent mA", id = 4),
            ChildFun(name = general.R.string.battery_capacity, body = "$batteryCapacity", id = 5),
            ChildFun(name = general.R.string.technologies, body = batteryTechnology, id = 6),
            ChildFun(name = general.R.string.battery_status, body = batteryHealth, id = 7),
            ChildFun(name = general.R.string.power_supply, body = chargingSource, id = 8),
        ))

    private fun Float.toF() = this * 1.8 + 32

    private fun round(value: Double, precision: Int = 1): Double {
        val scale = 10.0.pow(precision.toDouble()).toInt()
        return (value * scale).roundToInt().toDouble() / scale
    }
}