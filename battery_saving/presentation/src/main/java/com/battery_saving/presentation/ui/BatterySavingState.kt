package com.battery_saving.presentation.ui

data class BatterySavingState(
    val batteryChargePercent: Int = 0,
    val consumptionPercent: Int = 0,
    val calculateConsumption: Boolean = false
)