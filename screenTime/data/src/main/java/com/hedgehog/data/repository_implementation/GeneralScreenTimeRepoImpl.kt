package com.hedgehog.data.repository_implementation

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.hedgehog.data.R
import com.hedgehog.data.extensions.*
import com.hedgehog.data.utils.InitCalendars
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_DAY
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_WEEK
import com.hedgehog.domain.models.AppScreenTime
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.ScreenTimeDataRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class GeneralScreenTimeRepoImpl @Inject constructor(@ApplicationContext val context: Context) :
    ScreenTimeDataRepository {

    private val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()
    private var appScreenList: MutableList<AppScreenTime> = mutableListOf()

    override fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>> =
        flow {
            appScreenList.clear()
            getStatics(calendarScreenTime)
            appScreenList.apply { sortBy { it.totalTime }; reverse() }
            emit(CaseResult.Success(appScreenList))
        }

    private fun getStatics(calendarScreenTime: CalendarScreenTime) {
        val usageEventsGeneral =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageEvents =
            usageEventsGeneral.queryEvents(
                getBeginEndTime(calendarScreenTime).first,
                getBeginEndTime(calendarScreenTime).second
            )

        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }
        sortedEvents.forEach { (packageName, events) ->
            var startTime = 0L
            var closeTime = 0L
            var totalTime = 0L
            if (!context.isPackageExist(packageName) || !context.isCheckAppPackage(packageName)) return@forEach
            events.forEach { usageEvents ->
                when (usageEvents.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> startTime = usageEvents.timeStamp
                    UsageEvents.Event.ACTIVITY_PAUSED -> closeTime = usageEvents.timeStamp
                }
                if (startTime != 0L && closeTime != 0L) {
                    totalTime += closeTime - startTime
                    startTime = 0L
                    closeTime = 0L
                }
            }
            if (totalTime != 0L) {
                appScreenList.add(mapToAppScreenTime(packageName, totalTime))
                totalTime = 0L
            }
        }
        sortedEvents.clear()
    }

    private fun getBeginEndTime(calendarScreenTime: CalendarScreenTime): Pair<Long, Long> {
        val beginTime = InitCalendars().initBeginTime()
        val endTime = InitCalendars().initEndTime()

        if (calendarScreenTime.dataType == Calendar.DATE) {
            beginTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
            endTime.add(calendarScreenTime.dataType, -calendarScreenTime.endTime)
        } else {
            beginTime.add(Calendar.DATE, -ONE_WEEK)
            endTime.add(Calendar.DATE, +ONE_DAY)
        }
        return Pair(beginTime.timeInMillis, endTime.timeInMillis)
    }

    private fun mapToAppScreenTime(
        packageName: String,
        totalTime: Long
    ) = AppScreenTime(
        packageName = packageName,
        name = context.getAppLabel(packageName).toString(),
        time = mapTimeToString(totalTime),
        totalTime = totalTime,
        icon = context.getAppIcon(packageName),
        isItSystemApp = context.checkIsSystemApp(packageName)
    )

    private fun mapTimeToString(time: Long): String {
        val hour = (time / (1000 * 60 * 60))
        val minutes = ((time / (1000 * 60)) % 60)
        val second = time / 1000

        return when {
            hour == 0L && minutes == 0L && second == 0L -> context.getString(R.string.D_second, 1)
            hour == 0L && minutes == 0L && second != 0L -> context.getString(
                R.string.D_second,
                second
            )
            hour == 0L && minutes != 0L -> context.getString(R.string.D_minutes, minutes)
            else -> context.getString(R.string.D_hour_D_minutes, hour, minutes)
        }
    }
}
