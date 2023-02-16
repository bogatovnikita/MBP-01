package com.hedgehog.data.repository_implementation

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageStats
import android.util.Log
import com.hedgehog.data.R
import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Method
import java.util.*
import javax.inject.Inject

class AppInfoRepositoryImplementation @Inject constructor(
    @ApplicationContext val context: Context
) : AppInfoRepository {

    private lateinit var usageEventsGeneral: UsageStatsManager
    private val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

    private lateinit var hourBeginTime: Calendar
    private lateinit var hourEndTime: Calendar

    private var listHour: MutableList<Long> = mutableListOf()
    private var listHourForMilliseconds: MutableList<Long> = mutableListOf()

    private lateinit var dayBeginTime: Calendar
    private lateinit var dayEndTime: Calendar

    private var listDay: MutableList<Long> = mutableListOf()
    private var listDayForMilliseconds: MutableList<Long> = mutableListOf()

    override fun getAppInfo(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): Flow<CaseResult<AppInfo, String>> = flow {
        clearAllList()
        usageEventsGeneral =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        if (calendarScreenTime.dataType == Calendar.DATE) {
            emit(CaseResult.Success(createAppInfoForDay(packageName, calendarScreenTime)))
        } else {
            emit(CaseResult.Success(createAppInfoForWeek(packageName, calendarScreenTime)))
        }
    }

    private fun clearAllList() {
        listHour.clear()
        listHourForMilliseconds.clear()
        listDay.clear()
        listDayForMilliseconds.clear()
    }

    private fun createAppInfoForDay(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): AppInfo {
        hourAppInfo(packageName, calendarScreenTime)
        return AppInfo(
            nameApp = getAppLabel(packageName).toString(),
            icon = getAppIcon(packageName),
            listTime = mapLongToTime(listHour),
            lastLaunch = mapTimeToLastLaunch(packageName),
            data = "0",
            totalTimeUsage = mapTimeToTotalTimeUsage(listHourForMilliseconds.sum()),
            isSystemApp = checkIsSystemApp(packageName)
        )
    }

    private fun hourAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        initHourBeginEndTime(calendarScreenTime)
        for (i in 0..23) {
            calculationHourTime(i)
            searchStatics(packageName, calendarScreenTime)
        }
    }

    private fun calculationHourTime(hour: Int) {
        if (hour == 0) return
        hourBeginTime.set(Calendar.HOUR_OF_DAY, hour)
        hourEndTime.set(Calendar.HOUR_OF_DAY, hour + 1)
    }

    private fun initHourBeginEndTime(calendarScreenTime: CalendarScreenTime) {
        hourBeginTime = Calendar.getInstance()
        hourBeginTime.set(Calendar.HOUR_OF_DAY, 0)
        hourBeginTime.set(Calendar.MINUTE, 0)
        hourBeginTime.set(Calendar.SECOND, 0)
        hourBeginTime.set(Calendar.MILLISECOND, 0)

        hourEndTime = Calendar.getInstance()
        hourEndTime.set(Calendar.HOUR_OF_DAY, 1)
        hourEndTime.set(Calendar.MINUTE, 0)
        hourEndTime.set(Calendar.SECOND, 0)
        hourEndTime.set(Calendar.MILLISECOND, 0)

        hourBeginTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
        hourEndTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
    }

    private fun createAppInfoForWeek(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): AppInfo {
        dayAppInfo(packageName, calendarScreenTime)
        return AppInfo(
            nameApp = getAppLabel(packageName).toString(),
            icon = getAppIcon(packageName),
            listTime = mapLongToTime(listDay),
            lastLaunch = mapTimeToLastLaunch(packageName),
            data = "0",
            totalTimeUsage = mapTimeToTotalTimeUsage(listDayForMilliseconds.sum()),
            isSystemApp = checkIsSystemApp(packageName)
        )
    }

    private fun dayAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        initDayBeginEndTime()
        for (i in 0..7) {
            calculateDayTime(i)
            searchStatics(packageName, calendarScreenTime)
        }
    }

    private fun initDayBeginEndTime() {
        dayBeginTime = Calendar.getInstance()
        dayBeginTime.set(Calendar.HOUR_OF_DAY, 0)
        dayBeginTime.set(Calendar.MINUTE, 0)
        dayBeginTime.set(Calendar.SECOND, 0)
        dayBeginTime.set(Calendar.MILLISECOND, 0)

        dayEndTime = Calendar.getInstance()
        dayEndTime.set(Calendar.HOUR_OF_DAY, 0)
        dayEndTime.set(Calendar.MINUTE, 0)
        dayEndTime.set(Calendar.SECOND, -1)
        dayEndTime.set(Calendar.MILLISECOND, 0)

        dayBeginTime.add(Calendar.DATE, -8)
        dayEndTime.add(Calendar.DATE, -7)
    }

    private fun calculateDayTime(day: Int) {
        if (day == 0) return
        dayBeginTime.add(Calendar.DATE, +1)
        dayEndTime.add(Calendar.DATE, +1)
    }

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

    private fun getBatteryCharge(packageName: String) {
        getPackageSizeInfo.invoke(
            context.packageManager,
            packageName,
            object : IPackageStatsObserver.Stub() {
                override fun onGetStatsCompleted(pStats: PackageStats?, succeeded: Boolean) {
                    Log.e("pie", "onGetStatsCompleted: pStats=${pStats?.codeSize}")
                }
            })
    }

    private fun searchStatics(thisPackageName: String, calendarScreenTime: CalendarScreenTime) {
        val usageEvents = if (calendarScreenTime.dataType == Calendar.DATE) {
            usageEventsGeneral.queryEvents(hourBeginTime.timeInMillis, hourEndTime.timeInMillis)
        } else {
            usageEventsGeneral.queryEvents(dayBeginTime.timeInMillis, dayEndTime.timeInMillis)
        }

        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }

        if (!sortedEvents.keys.contains(thisPackageName)) {
            if (calendarScreenTime.dataType == Calendar.DATE) {
                listHour.add(0)
                listHourForMilliseconds.add(0)
            } else {
                listDay.add(0)
                listDayForMilliseconds.add(0)
            }
        } else {
            sortedEvents.forEach { (packageName, events) ->
                var startTime = 0L
                var closeTime = 0L
                var totalTime = 0L
                if (thisPackageName != packageName) return@forEach
                events.forEach { usageEvents ->
                    when (usageEvents.eventType) {
                        UsageEvents.Event.ACTIVITY_RESUMED -> startTime = usageEvents.timeStamp
                        UsageEvents.Event.ACTIVITY_PAUSED -> closeTime = usageEvents.timeStamp
                    }
                    if (startTime != 0L && closeTime != 0L) {
//                        Log.e("pie", "hourBeginTime: ${hourBeginTime.time}")
//                        Log.e("pie", "hourEndTime: ${hourEndTime.time}")
//                        Log.e(
//                            "pie",
//                            "startTime = ${startTime - 1_675_378_000_000} // closeTime = ${closeTime - 1_675_378_000_000}"
//                        )
                        totalTime += closeTime - startTime
//                        Log.e("pie", "total = $totalTime")
//                        Log.e("pie", "_____________________________________________")
                        startTime = 0L
                        closeTime = 0L
                    }
                }
                if (calendarScreenTime.dataType == Calendar.DATE) {
                    listHour.add(totalTime)
                    listHourForMilliseconds.add(totalTime)
                } else {
                    listDay.add(totalTime)
                    listDayForMilliseconds.add(totalTime)
                }
                totalTime = 0L
            }
            sortedEvents.clear()
        }
    }

    private fun mapTimeToLastLaunch(thisPackageName: String): String {
        val listLastLaunchApp = mutableListOf<Long>()
        val totalEndLastLaunchTime = Calendar.getInstance()
        val totalBeginLastLaunchTime = Calendar.getInstance()
        totalBeginLastLaunchTime.set(Calendar.DATE, -20)

        val usageEvents =
            usageEventsGeneral.queryEvents(
                totalBeginLastLaunchTime.timeInMillis,
                totalEndLastLaunchTime.timeInMillis
            )
        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }

        sortedEvents.forEach { (packageName, events) ->
            if (thisPackageName != packageName) return@forEach
            events.forEach { usageEvents ->
                if (usageEvents.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    listLastLaunchApp.add(usageEvents.timeStamp)
                }
            }
        }

        sortedEvents.clear()
        return try {
            val date = System.currentTimeMillis() - listLastLaunchApp.last()
            val day = date / (60 * 60 * 24 * 1000)
            val hour = (date / (1000 * 60 * 60)) % 24
            val minutes = ((date / (1000 * 60)) % 60)
            val second = (date / 1000) % 60
            when {
                day != 0L -> context.getString(R.string.D_day_D_hour_ago, day, hour)
                day == 0L && hour != 0L -> context.getString(
                    R.string.D_hour_D_minutes_ago,
                    hour,
                    minutes
                )
                day == 0L && hour == 0L && minutes != 0L -> context.getString(
                    R.string.D_minute_D_second_ago,
                    minutes,
                    second
                )
                day == 0L && hour == 0L && minutes == 0L -> context.getString(
                    R.string.D_second_ago, second
                )
                else -> context.getString(R.string.is_unknown)
            }
        } catch (e: Exception) {
            context.getString(R.string.is_unknown)
        }
    }

    private fun mapTimeToTotalTimeUsage(time: Long): String {
        val hour = (time / (1000 * 60 * 60))
        val minutes = ((time / (1000 * 60)) % 60)
        val second = time / 1000
        return if (hour == 0L && minutes == 0L) {
            context.getString(R.string.D_second, second)
        } else {
            context.getString(R.string.D_hour_D_minutes, hour, minutes)
        }
    }

    private fun mapLongToTime(time: MutableList<Long>): MutableList<Int> {
        val newList = mutableListOf<Int>()
        time.forEach {
            if (it == 0L) {
                newList.add(0)
            } else {
                newList.add((it / 60_000).toInt())
            }
        }
        return newList
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

    var getPackageSizeInfo: Method = context.packageManager.javaClass.getMethod(
        "getPackageSizeInfo", String::class.java, IPackageStatsObserver::class.java
    )
}