package com.entertainment.event.ssearch.data.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class RestartServiceReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workManager: WorkManager = WorkManager.getInstance(context)
        val startServiceRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(ServiceRestartWorker::class.java)
            .build()
        workManager.enqueue(startServiceRequest)
    }

}