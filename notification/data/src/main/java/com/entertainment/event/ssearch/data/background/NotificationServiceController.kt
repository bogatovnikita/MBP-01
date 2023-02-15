package com.entertainment.event.ssearch.data.background

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.entertainment.event.ssearch.data.background.NotificationCleanBroadcastReceiver.Companion.ACTION_CLEAR_NOTIFICATIONS
import com.entertainment.event.ssearch.domain.permission.Permission
import com.entertainment.event.ssearch.domain.service.ServiceController
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationServiceController @Inject constructor(
    private val context: Application,
    private val permission: Permission,
): ServiceController {

    override fun startServiceIfNeed() {
        if (!permission.hasServicePermission()) return
        if (NotificationService.isServiceRunning()) return
        val intent = Intent(context, NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    override fun cleanAllNotification() = context.sendBroadcast(Intent(ACTION_CLEAR_NOTIFICATIONS))

    override fun setRestartServiceWorker() {
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker"
        val workManager: WorkManager = WorkManager.getInstance(context)
        val request: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            ServiceRestartWorker::class.java,
            16,
            TimeUnit.MINUTES
        )
            .build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

}