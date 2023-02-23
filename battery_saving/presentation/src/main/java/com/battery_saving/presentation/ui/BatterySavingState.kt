package com.battery_saving.presentation.ui

import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity

data class BatterySavingState(
    val batteryChargePercent: Int = 0,
    val consumptionPercent: Int = 0,
    val listConsumptionPercent: List<BatteryChargeStatisticsEntity> = listOf(),
    val calculateConsumption: Boolean = false
)