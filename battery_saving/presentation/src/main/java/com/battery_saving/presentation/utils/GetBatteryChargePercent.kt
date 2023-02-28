package com.battery_saving.presentation.utils

import android.content.Context
import android.os.BatteryManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBatteryChargePercent @Inject constructor(@ApplicationContext val context: Context) {

    fun getBatteryChargePercent(): Flow<Int> = flow {
        val percent = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val value = percent.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        emit(value)
    }
}