package com.entertainment.event.ssearch.data.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationCleanBroadcastReceiver : BroadcastReceiver() {

    private val _clearAll = MutableStateFlow(START_OBSERVE)
    val clearAll = _clearAll.asStateFlow()

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action ?: return

        //TODO зачем кладется в стейт рандомное число
        //TODO ответ: не нашел лучше способа генерить всегда уникальные события
        if (action == ACTION_CLEAR_NOTIFICATIONS) {
            _clearAll.value = Math.random().toString()
        }
    }

    companion object {
        const val ACTION_CLEAR_NOTIFICATIONS = "ACTION_CLEAR_NOTIFICATIONS"
        const val START_OBSERVE = "START_OBSERVE"
    }


}