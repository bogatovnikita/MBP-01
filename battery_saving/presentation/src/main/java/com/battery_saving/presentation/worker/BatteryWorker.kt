package com.battery_saving.presentation.worker

import android.content.Context
import android.os.BatteryManager
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.battery_saving.presentation.room.BatteryChargeDAO
import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BatteryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val database: BatteryChargeDAO
) : Worker(context, workerParams) {
    private val percent: BatteryManager

    init {
        percent = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    override fun doWork(): Result {
        database.setBatteryChargeStatics(
            BatteryChargeStatisticsEntity(
                time = System.currentTimeMillis(),
                percentCharge = 666999
            )
        )
        return Result.success()
    }
}