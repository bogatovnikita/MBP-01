package com.entertainment.event.ssearch.data.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.coroutineScope
import com.entertainment.event.ssearch.data.providers.SettingsProviderImpl
import com.entertainment.event.ssearch.data.repositories.AppRepositoryImpl
import com.entertainment.event.ssearch.data.repositories.NotificationRepositoryImpl
import com.entertainment.event.ssearch.data.repositories.mapToNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService: NotificationListenerService(), LifecycleOwner {

    @Inject
    lateinit var notifications: NotificationRepositoryImpl

    @Inject
    lateinit var apps: AppRepositoryImpl

    @Inject
    lateinit var settings: SettingsProviderImpl

    private val dispatcher = ServiceLifecycleDispatcher(this)

    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        dispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        dispatcher.onServicePreSuperOnStart()
        createNotificationChannel()
        startForeground(BASE_CHANNEL_ID, createPersistNotification())
        return START_STICKY
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
                val isSwitched = try {
                    apps.readApp(notification.packageName).isSwitched
                } catch (e: Exception) {
                    false
                }
                if (isSwitched || settings.isDisturbModeSwitched()) {
                    notifications.insert(notification.mapToNotification())
                    cancelNotification(notification.key)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(general.R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel =
                NotificationChannel(PERSIST_NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPersistNotification(): Notification =
        NotificationCompat.Builder(this, PERSIST_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(general.R.string.app_name))
            .build()

    companion object{
        const val BASE_CHANNEL_ID = 1
        const val PERSIST_NOTIFICATION_CHANNEL_ID = "PERSIST_NOTIFICATION_CHANNEL_ID"
    }

}