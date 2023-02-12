package com.entertainment.event.ssearch.data.dnd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.entertainment.event.ssearch.data.dnd.DNDAutoModeControllerImpl.Companion.ALARM_EXTRA_ACTION
import com.entertainment.event.ssearch.data.dnd.DNDAutoModeControllerImpl.Companion.DAY_OF_WEEK_EXTRA
import com.entertainment.event.ssearch.data.dnd.DNDAutoModeControllerImpl.Companion.HOURS_EXTRA
import com.entertainment.event.ssearch.data.dnd.DNDAutoModeControllerImpl.Companion.MINUTES_EXTRA
import com.entertainment.event.ssearch.domain.dnd.DNDAutoModeController
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase.Companion.DND_OFF
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase.Companion.DND_ON
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class DNDReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dndController: DNDControllerImpl
    @Inject
    lateinit var dndAutoModeController: DNDAutoModeController

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getIntExtra(ALARM_EXTRA_ACTION, -1)
        val hours = intent.getIntExtra(HOURS_EXTRA, -1)
        val minutes = intent.getIntExtra(MINUTES_EXTRA, -1)
        val dayOfWeek = intent.getIntExtra(DAY_OF_WEEK_EXTRA, -1)

        when (action) {
            DND_ON -> goAsync {
                dndController.setDNDModeOn()
                dndAutoModeController.setSingleAlarm(dayOfWeek, hours, action, minutes)
            }
            DND_OFF -> goAsync {
                dndController.setDNDModeOff()
                dndAutoModeController.setSingleAlarm(dayOfWeek, hours, action, minutes)
            }
        }

    }
}

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}