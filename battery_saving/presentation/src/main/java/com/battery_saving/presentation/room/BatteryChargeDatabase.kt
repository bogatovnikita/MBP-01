package com.battery_saving.presentation.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity


@Database(
    version = 1,
    entities = [
        BatteryChargeStatisticsEntity::class
    ]
)
abstract class BatteryChargeDatabase : RoomDatabase() {

    abstract fun getBatteryChargeStatisticsDao(): BatteryChargeDAO

    companion object {

        const val DB_BATTERY = "DB_BATTERY"

        private var batteryDatabase: BatteryChargeDatabase? = null

        fun create(context: Context): BatteryChargeDatabase {

            return if (batteryDatabase == null) {
                batteryDatabase = Room.databaseBuilder(
                    context,
                    BatteryChargeDatabase::class.java,
                    DB_BATTERY
                ).build()
                batteryDatabase!!
            } else {
                batteryDatabase!!
            }

        }
    }
}