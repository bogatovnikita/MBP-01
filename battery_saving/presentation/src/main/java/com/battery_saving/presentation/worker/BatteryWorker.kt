package com.battery_saving.presentation.worker

import android.content.Context
import android.os.BatteryManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.battery_saving.presentation.room.BatteryChargeDAO
import com.battery_saving.presentation.room.BatteryChargeDatabase
import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity

class BatteryWorker(
    private val context: Context, workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val database = BatteryChargeDatabase.create(context).getBatteryChargeStatisticsDao()
        database.setBatteryChargeStatics(
            BatteryChargeStatisticsEntity(
                time = System.currentTimeMillis(),
                percentCharge = getPercentCharge()
            )
        )
        if (database.getDatabase().size > 10) clearDatabase(database)
        return Result.success()
    }

    private fun getPercentCharge(): Int {
        val percent = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return percent.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun clearDatabase(database: BatteryChargeDAO) {
        val list = database.getDatabase().toMutableList()
        list.subList(0, 6).clear()
        database.clearTable()
        list.forEach {
            database.setBatteryChargeStatics(it)
        }
    }
}