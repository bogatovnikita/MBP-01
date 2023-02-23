package com.battery_saving.presentation.utils

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.battery_saving.presentation.worker.BatteryWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetBatteryWorker @Inject constructor(@ApplicationContext val context: Context) {

    fun getWorker() {
        val workerName = "Start battery charge worker"
        val workManager = WorkManager.getInstance(context)
        val request = PeriodicWorkRequest.Builder(
            BatteryWorker::class.java,
            16,
            TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            workerName,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}