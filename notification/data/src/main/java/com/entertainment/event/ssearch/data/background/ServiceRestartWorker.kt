package com.entertainment.event.ssearch.data.background

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.*

class ServiceRestartWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        if (!NotificationService.isServiceRunning()) {
            val intent = Intent(context, NotificationService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
        return Result.success()
    }

}