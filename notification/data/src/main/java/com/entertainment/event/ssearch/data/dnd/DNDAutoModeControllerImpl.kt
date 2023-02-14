package com.entertainment.event.ssearch.data.dnd

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.entertainment.event.ssearch.domain.dnd.DNDAutoModeController
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase.Companion.DND_OFF
import java.util.*
import javax.inject.Inject

class DNDAutoModeControllerImpl @Inject constructor(
    private val context: Application,
) : DNDAutoModeController {

    private val remainingWeekDays = mutableListOf(1, 2, 3, 4, 5, 6, 7)

    override fun setRepeatAlarm(
        hours: Int,
        action: Int,
        minutes: Int,
        daysOfWeek: List<Int>,
    ) {

        val alarmMgr = getAlarmManager()

        setNewAlarms(daysOfWeek, action, hours, minutes, alarmMgr)

        deleteOldAlarms(action, alarmMgr, hours, minutes)
    }

    override fun setSingleAlarm(
        day: Int,
        hours: Int,
        action: Int,
        minutes: Int
    ) {
        val alarmManager = getAlarmManager()
        val requestCode =
            if (action == DND_OFF) getRequestCodeEndAlarm() else getRequestCodeStartAlarm()
        val pending =
            pendingIntent(action, requestCode[day]!!, hours, minutes, day)
        setScheduleAlarm(hours, minutes, day, alarmManager, pending, true)
    }

    private fun setNewAlarms(
        daysOfWeek: List<Int>,
        action: Int,
        hours: Int,
        minutes: Int,
        alarmMgr: AlarmManager
    ) {
        daysOfWeek.forEach { day ->
            remainingWeekDays.remove(day)
            val requestCode =
                if (action == DND_OFF) getRequestCodeEndAlarm() else getRequestCodeStartAlarm()
            val pending =
                pendingIntent(action, requestCode[day]!!, hours, minutes, day)
            setScheduleAlarm(hours, minutes, day, alarmMgr, pending, false)
        }
    }

    private fun deleteOldAlarms(
        action: Int, alarmMgr: AlarmManager,
        hours: Int,
        minutes: Int,
    ) {
        remainingWeekDays.forEach { day ->
            val requestCode =
                if (action == DND_OFF) getRequestCodeEndAlarm() else getRequestCodeStartAlarm()
            val pending =
                pendingIntent(action, requestCode[day]!!, hours, minutes, day)
            alarmMgr.cancel(pending)
        }
    }

    private fun getAlarmManager(): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun setScheduleAlarm(
        hours: Int,
        minutes: Int,
        dayOfWeek: Int,
        alarmMgr: AlarmManager,
        alarmIntent: PendingIntent?,
        isRollOnNextWeek: Boolean,
    ) {

        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, hours)
        datetimeToAlarm.set(Calendar.MINUTE, minutes)
        datetimeToAlarm.set(Calendar.SECOND, 0)
        datetimeToAlarm.set(Calendar.MILLISECOND, 0)
        datetimeToAlarm.set(Calendar.DAY_OF_WEEK, dayOfWeek)

        val today = Calendar.getInstance(Locale.getDefault())

        if (shouldNotifyToday(dayOfWeek, today, datetimeToAlarm) && !isRollOnNextWeek) {
            alarmMgr.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    datetimeToAlarm.timeInMillis,
                    alarmIntent
                ), alarmIntent
            )
            return
        }

        if (Calendar.getInstance(Locale.getDefault()).timeInMillis > datetimeToAlarm.timeInMillis) {
            datetimeToAlarm.roll(Calendar.WEEK_OF_YEAR, 1)
        }
        alarmMgr.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                datetimeToAlarm.timeInMillis,
                alarmIntent
            ), alarmIntent
        )
    }

    private fun pendingIntent(
        action: Int,
        requestCode: Int,
        hours: Int,
        minutes: Int,
        dayOfWeek: Int,
    ): PendingIntent? {
        val intent = alarmIntent(action, requestCode, hours, minutes, dayOfWeek)
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun alarmIntent(
        action: Int,
        requestCode: Int,
        hours: Int,
        minutes: Int,
        dayOfWeek: Int,
    ): Intent =
        Intent(context, DNDReceiver::class.java).apply {
            putExtra(ALARM_EXTRA_ACTION, action)
            putExtra(REQUEST_CODE, requestCode)
            putExtra(HOURS_EXTRA, hours)
            putExtra(MINUTES_EXTRA, minutes)
            putExtra(DAY_OF_WEEK_EXTRA, dayOfWeek)
        }


    private fun shouldNotifyToday(
        dayOfWeek: Int,
        today: Calendar,
        datetimeToAlarm: Calendar
    ): Boolean {
        return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                today.get(Calendar.HOUR_OF_DAY) <= datetimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= datetimeToAlarm.get(Calendar.MINUTE)
    }

    companion object {
        const val ALARM_EXTRA_ACTION = "ALARM_EXTRA_ACTION"
        const val HOURS_EXTRA = "HOURS_EXTRA"
        const val MINUTES_EXTRA = "MINUTES_EXTRA"
        const val DAY_OF_WEEK_EXTRA = "DAY_OF_WEEK_EXTRA"
        const val REQUEST_CODE = "REQUEST_CODE"

        fun getRequestCodeStartAlarm() = mapOf(
            1 to 11,
            2 to 12,
            3 to 13,
            4 to 14,
            5 to 15,
            6 to 16,
            7 to 17,
        )

        fun getRequestCodeEndAlarm() = mapOf(
            1 to 21,
            2 to 22,
            3 to 23,
            4 to 24,
            5 to 25,
            6 to 26,
            7 to 27,
        )
    }

}