package com.hedgehog.data.repository_implementation

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import com.hedgehog.data.R
import com.hedgehog.domain.models.AppScreenTime
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.ScreenTimeDataRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class ScreenTimeDataRepositoryImplementation @Inject constructor(@ApplicationContext val context: Context) :
    ScreenTimeDataRepository {

    private val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()
    private lateinit var usageEventsGeneral: UsageStatsManager

    private var appScreenList: MutableList<AppScreenTime> = mutableListOf()
    private lateinit var beginTime: Calendar
    private lateinit var endTime: Calendar

    override fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>> =
        flow {
            initBeginEndTime(calendarScreenTime)
            usageEventsGeneral =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            appScreenList.clear()
            checkStatics()
            appScreenList.apply { sortBy { it.totalTime }; reverse() }
            emit(CaseResult.Success(appScreenList))
        }

    private fun initBeginEndTime(calendarScreenTime: CalendarScreenTime) {
        beginTime = Calendar.getInstance()
        beginTime.set(Calendar.HOUR_OF_DAY, 0)
        beginTime.set(Calendar.MINUTE, 0)
        beginTime.set(Calendar.SECOND, 0)
        beginTime.set(Calendar.MILLISECOND, 0)

        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY, 0)
        endTime.set(Calendar.MINUTE, 0)
        endTime.set(Calendar.SECOND, -1)
        endTime.set(Calendar.MILLISECOND, 0)

        if (calendarScreenTime.dataType == Calendar.DATE) {
            beginTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
            endTime.add(calendarScreenTime.dataType, -calendarScreenTime.endTime)
        } else {
            beginTime.set(Calendar.DAY_OF_WEEK, beginTime.firstDayOfWeek)
            beginTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.beginTime)
            endTime.set(Calendar.DAY_OF_WEEK, beginTime.firstDayOfWeek)
            endTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.endTime)
        }
    }

    private fun checkStatics() {
        val usageEvents =
            usageEventsGeneral.queryEvents(beginTime.timeInMillis, endTime.timeInMillis)

        if (usageEvents.hasNextEvent()) {
            getStatUseEvents(usageEvents)
        } else {
            searchStatisticsFromUsageStats()
        }
    }

    private fun getStatUseEvents(usageEvents: UsageEvents) {
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

    private fun searchStatisticsFromUsageStats() {
        usageEventsGeneral.queryAndAggregateUsageStats(
            beginTime.timeInMillis,
            endTime.timeInMillis
        ).values.filter {
            it.totalTimeInForeground > 1000 && context.isPackageExist(it.packageName) && context.isCheckAppPackage(
                it.packageName
            )
        }.forEach {
            appScreenList.add(
                mapToAppScreenTime(
                    it.packageName,
                    it.totalTimeInForeground
                )
            )
        }
    }

    private fun mapTimeToString(time: Long): String {
        val hour = (time / (1000 * 60 * 60))
        val minutes = ((time / (1000 * 60)) % 60)
        val second = time / 1000
        return if (hour == 0L && minutes == 0L && second != 0L) {
            context.getString(R.string.D_second, second)
        } else if (hour == 0L && minutes == 0L && second == 0L) {
            context.getString(R.string.D_second, 1)
        } else {
            context.getString(R.string.D_hour_D_minutes, hour, minutes)
        }
    }

    private fun mapToAppScreenTime(
        packageName: String,
        totalTime: Long
    ) = AppScreenTime(
        packageName = packageName,
        name = getAppLabel(packageName).toString(),
        time = mapTimeToString(totalTime),
        totalTime = totalTime,
        icon = getAppIcon(packageName),
        isItSystemApp = checkIsSystemApp(packageName)
    )

    private fun getAppLabel(packageName: String) = try {
        context.packageManager.getApplicationInfo(packageName, 0)
            .loadLabel(context.packageManager)
    } catch (e: Exception) {
        packageName
    }

    private fun getAppIcon(packageName: String) = try {
        context.packageManager.getApplicationIcon(packageName)
    } catch (e: Exception) {
        null
    }

    private fun checkIsSystemApp(packageName: String) = try {
        context.packageManager.getApplicationInfo(
            packageName, 0
        ).flags and ApplicationInfo.FLAG_SYSTEM != 0 || context.packageManager.getApplicationInfo(
            packageName, 0
        ).flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
    } catch (e: Exception) {
        true
    }
}

fun Context.isPackageExist(target: String): Boolean {
    return packageManager.getInstalledApplications(0)
        .find { info -> info.packageName == target } != null
}

fun Context.isCheckAppPackage(target: String): Boolean {
    return target != this.packageName
}
