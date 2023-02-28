package com.battery_saving.presentation.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity

@Dao
interface BatteryChargeDAO {

    @Insert
    fun setBatteryChargeStatics(batteryChargeStatisticsEntity: BatteryChargeStatisticsEntity)

    @Query("SELECT * FROM battery_charge_statistics")
    fun getDatabase(): List<BatteryChargeStatisticsEntity>

    @Query("DELETE FROM battery_charge_statistics")
    fun clearTable()
}