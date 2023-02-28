package com.battery_saving.presentation.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battery_charge_statistics")
data class BatteryChargeStatisticsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val time: Long,
    @ColumnInfo(name = "percent_charge") val percentCharge: Int
)
