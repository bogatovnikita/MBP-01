package com.hedgehog.data.repository_implementation

import android.app.usage.StorageStatsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageStats
import android.os.Build
import android.text.format.Formatter
import com.hedgehog.data.R
import com.hedgehog.data.extensions.checkIsSystemApp
import com.hedgehog.data.extensions.getAppIcon
import com.hedgehog.data.extensions.getAppLabel
import com.hedgehog.data.utils.InitCalendars
import com.hedgehog.data.utils.InitCalendars.Companion.HOURS_IN_DAY
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_DAY
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_DAY_MILLISECONDS
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_HOUR
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_HOUR_MILLISECONDS
import com.hedgehog.data.utils.InitCalendars.Companion.ONE_WEEK
import com.hedgehog.data.utils.InitCalendars.Companion.SIX_DAYS
import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.models.Period
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Method
import java.util.*
import javax.inject.Inject

class DetailsScreenTimeRepoImpl @Inject constructor(
    @ApplicationContext val context: Context
) : AppInfoRepository {

    private lateinit var usageEventsGeneral: UsageStatsManager
    private lateinit var storageStatsManager: StorageStatsManager
    private val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

    private lateinit var hourBeginTime: Calendar
    private lateinit var hourEndTime: Calendar

    private var listHour: MutableList<Long> = mutableListOf()

    private lateinit var dayBeginTime: Calendar
    private lateinit var dayEndTime: Calendar

    private var listDay: MutableList<Long> = mutableListOf()

    private var isEndOnResume = false

    override fun getAppInfo(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): Flow<CaseResult<AppInfo, String>> = flow {
        clearAllList()
        usageEventsGeneral =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        if (calendarScreenTime.dataType == Calendar.DATE) {
            emit(
                CaseResult.Success(
                    createAppInfo(packageName, calendarScreenTime, Period.Day, listHour)
                )
            )
        } else {
            emit(
                CaseResult.Success(
                    createAppInfo(packageName, calendarScreenTime, Period.Week, listDay)
                )
            )
        }
    }

    private fun clearAllList() {
        listHour.clear()
        listDay.clear()
    }

    private fun createAppInfo(
        packageName: String,
        calendarScreenTime: CalendarScreenTime,
        period: Period,
        list: MutableList<Long>
    ): AppInfo {
        when (period) {
            Period.Day -> hourAppInfo(packageName, calendarScreenTime)
            Period.Week -> dayAppInfo(packageName)
        }
        return AppInfo(
            nameApp = context.getAppLabel(packageName).toString(),
            icon = context.getAppIcon(packageName),
            listTime = mapLongToTime(list),
            lastLaunch = mapTimeToLastLaunch(packageName),
            data = getUsedData(packageName),
            totalTimeUsage = mapTimeToTotalTimeUsage(list.sum()),
            isSystemApp = context.checkIsSystemApp(packageName)
        )
    }

    private fun hourAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        initHourBeginEndTime(calendarScreenTime)
        for (i in 0..HOURS_IN_DAY) {
            calculationHourTime(i)
            searchStaticsForHours(packageName)
        }
    }

    private fun initHourBeginEndTime(calendarScreenTime: CalendarScreenTime) {
        hourBeginTime = InitCalendars().initBeginTime()
        hourEndTime = InitCalendars().initEndTime()

        hourEndTime.set(Calendar.HOUR_OF_DAY, ONE_HOUR)

        hourBeginTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
        hourEndTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
    }

    private fun calculationHourTime(hour: Int) {
        if (hour == 0) return
        hourBeginTime.set(Calendar.HOUR_OF_DAY, hour)
        hourEndTime.set(Calendar.HOUR_OF_DAY, hour)
    }

    private fun dayAppInfo(packageName: String) {
        initDayBeginEndTime()
        for (i in 0..ONE_WEEK) {
            calculateDayTime(i)
            searchStaticsForDays(packageName)
        }
    }

    private fun initDayBeginEndTime() {
        dayBeginTime = InitCalendars().initBeginTime()
        dayEndTime = InitCalendars().initEndTime()

        dayBeginTime.add(Calendar.DATE, -ONE_WEEK)
        dayEndTime.add(Calendar.DATE, -SIX_DAYS)
    }

    private fun calculateDayTime(day: Int) {
        if (day == 0) return
        dayBeginTime.add(Calendar.DATE, +ONE_DAY)
        dayEndTime.add(Calendar.DATE, +ONE_DAY)
    }

    private fun searchStaticsForHours(thisPackageName: String) {
        val usageEvents =
            usageEventsGeneral.queryEvents(hourBeginTime.timeInMillis, hourEndTime.timeInMillis)

        getAllEvents(usageEvents)

        if (!sortedEvents.keys.contains(thisPackageName) && !isEndOnResume) {
            listHour.add(0)

        } else if (isEndOnResume && !sortedEvents.keys.contains(thisPackageName)) {
            listHour.add(ONE_HOUR_MILLISECONDS)

        } else {
            sortedEvents.filter { it.key == thisPackageName }.forEach { (packageName, events) ->
                var startTime = hourBeginTime.timeInMillis
                var closeTime = hourEndTime.timeInMillis
                var totalTime = 0L
                events.forEachIndexed { index, usageEvents ->
                    when (usageEvents.eventType) {
                        UsageEvents.Event.ACTIVITY_RESUMED -> startTime = usageEvents.timeStamp
                        UsageEvents.Event.ACTIVITY_PAUSED -> closeTime = usageEvents.timeStamp
                    }
                    if (index == events.lastIndex && usageEvents.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        totalTime += hourEndTime.timeInMillis - startTime
                        isEndOnResume = true
                    } else {
                        isEndOnResume = false
                    }
                    if (startTime != 0L && closeTime != 0L && closeTime != hourEndTime.timeInMillis) {
                        totalTime += closeTime - startTime
                        startTime = 0L
                        closeTime = 0L
                    }
                }
                if (totalTime > ONE_HOUR_MILLISECONDS) {
                    listHour.add(ONE_HOUR_MILLISECONDS)
                } else {
                    listHour.add(totalTime)
                }
                totalTime = 0L
            }
            sortedEvents.clear()
        }
    }

    private fun getAllEvents(usageEvents: UsageEvents) {
        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
            packageEvents.add(event)
            sortedEvents[event.packageName] = packageEvents
        }
    }

    private fun searchStaticsForDays(thisPackageName: String) {
        val usageEvents =
            usageEventsGeneral.queryEvents(dayBeginTime.timeInMillis, dayEndTime.timeInMillis)

        getAllEvents(usageEvents)

        if (!sortedEvents.keys.contains(thisPackageName)) {
            listDay.add(0)
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
                        totalTime += closeTime - startTime
                        startTime = 0L
                        closeTime = 0L
                    }
                }
                if (totalTime > ONE_DAY_MILLISECONDS) {
                    listDay.add(ONE_DAY_MILLISECONDS)
                } else {
                    listDay.add(totalTime)
                }
                totalTime = 0L
            }
            sortedEvents.clear()
        }
    }

    private fun getUsedData(packageName: String): String {
        var cacheSize = 0L
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
                storageStatsManager =
                    context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                val storage = storageStatsManager.queryStatsForUid(appInfo.storageUuid, appInfo.uid)
                cacheSize =
                    storage.cacheBytes + storage.dataBytes
                return Formatter.formatFileSize(context, cacheSize)
            } else {
                getPackageSizeInfo().invoke(
                    context.packageManager,
                    packageName,
                    object : IPackageStatsObserver.Stub() {
                        override fun onGetStatsCompleted(
                            pStats: PackageStats?,
                            succeeded: Boolean
                        ) {
                            pStats ?: return
                            cacheSize = pStats.cacheSize
                        }
                    })
                return Formatter.formatFileSize(context, cacheSize)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return context.getString(R.string.is_unknown)
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
        getAllEvents(usageEvents)

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

    fun getPackageSizeInfo(): Method {
        return context.packageManager.javaClass.getMethod(
            "getPackageSizeInfo", String::class.java, IPackageStatsObserver::class.java
        )
    }
}