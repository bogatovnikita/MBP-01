package com.battery_saving.presentation.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
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
                percentCharge = 666999
            )
        )
        return Result.success()
    }
}