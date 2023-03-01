package com.entertainment.event.ssearch.data.device_battery_info

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryChargeReceiver @Inject constructor(): BroadcastReceiver() {

    private val _batteryPercent: MutableStateFlow<Int> = MutableStateFlow(0)
    val batteryPercent = _batteryPercent.asStateFlow()

    override fun onReceive(ctxt: Context?, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level * 100 / scale
        _batteryPercent.value = batteryPct
    }

    fun registerReceiver(app: Application) {
        app.registerReceiver(
            this,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    fun unregisterReceiver(app: Application) {
        try {
            app.unregisterReceiver(this)
        } catch (e: Exception){}
    }
}