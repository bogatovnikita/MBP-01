package com.entertainment.event.ssearch.data.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.coroutineScope
import com.entertainment.event.ssearch.data.background.NotificationCleanBroadcastReceiver.Companion.START_OBSERVE
import com.entertainment.event.ssearch.data.providers.SettingsImpl
import com.entertainment.event.ssearch.data.repositories.Apps
import com.entertainment.event.ssearch.data.repositories.Notifications
import com.entertainment.event.ssearch.data.repositories.mapToNotificationDb
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : NotificationListenerService(), LifecycleOwner {

    @Inject
    lateinit var notifications: Notifications

    @Inject
    lateinit var apps: Apps

    @Inject
    lateinit var settings: SettingsImpl

    private val dispatcher = ServiceLifecycleDispatcher(this)

    private val reciver by lazy { NotificationCleanBroadcastReceiver() }

    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        registerReceiver(
            reciver,
            IntentFilter(NotificationCleanBroadcastReceiver.ACTION_CLEAR_NOTIFICATIONS)
        )
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        dispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        dispatcher.onServicePreSuperOnStart()
        serviceStart()
        createNotificationChannel()
        startForeground(BASE_CHANNEL_ID, createPersistNotification())
        observeClearEvent()
        return START_STICKY
    }

    private fun observeClearEvent() {
        dispatcher.lifecycle.coroutineScope.launch {
            reciver.clearAll.collect {
                if (it != START_OBSERVE) {
                    cancelAllNotifications()
                }
            }
        }
    }

    override fun getLifecycle(): Lifecycle = dispatcher.lifecycle

    override fun onDestroy() {
        dispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        saveNotification()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        saveNotification()
    }

    private fun saveNotification() {
        dispatcher.lifecycle.coroutineScope.launch {
            activeNotifications.forEach { notification ->
                val isSwitched = apps.readApp(notification.packageName)?.isSwitched ?: false
                if (isSwitched || settings.isDisturbModeSwitched()) {
                    notifications.insert(notification.mapToNotificationDb())
                    cancelNotification(notification.key)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(general.R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel =
                NotificationChannel(PERSIST_NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPersistNotification(): Notification =
        NotificationCompat.Builder(this, PERSIST_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(general.R.string.app_name))
            .build()

    override fun stopService(name: Intent?): Boolean {
        unregisterReceiver(reciver)
        serviceStop()
        return super.stopService(name)
    }

    companion object {
        const val BASE_CHANNEL_ID = 1
        const val PERSIST_NOTIFICATION_CHANNEL_ID = "PERSIST_NOTIFICATION_CHANNEL_ID"

        private var isServiceRunning = false

        //TODO подумать над неймингом и сделать private
        fun serviceStop() {
            isServiceRunning = false
        }

        //TODO подумать над неймингом и сделать private
        fun serviceStart() {
            isServiceRunning = true
        }

        fun isServiceRunning() = isServiceRunning
    }

}