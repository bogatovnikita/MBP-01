package com.hedgehog.data.repository_implementation

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.hedgehog.data.R
import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class AppInfoRepositoryImplementation @Inject constructor(@ApplicationContext val context: Context) :
    AppInfoRepository {

    private lateinit var stats: UsageStatsManager

    private lateinit var totalBeginTime: Calendar
    private lateinit var totalEndTime: Calendar

    private lateinit var hourBeginTime: Calendar
    private lateinit var hourEndTime: Calendar
    private var listHour: MutableList<Int> = mutableListOf()

    private lateinit var dayBeginTime: Calendar
    private lateinit var dayEndTime: Calendar
    private var listDay: MutableList<Int> = mutableListOf()


    override fun getAppInfo(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): Flow<CaseResult<AppInfo, String>> = flow {
        initTotalBeginEndTime(calendarScreenTime)

        stats = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val statsList = stats.queryAndAggregateUsageStats(
            totalBeginTime.timeInMillis, totalEndTime.timeInMillis
        )
        val checkPackage = statsList.filter { it.key == packageName }

        if (checkPackage.isNotEmpty()) {
            when (calendarScreenTime.dataType) {
                Calendar.DATE -> emit(
                    CaseResult.Success(
                        createAppInfoForDay(
                            packageName,
                            calendarScreenTime,
                            checkPackage.values.first()
                        )
                    )
                )
                Calendar.WEEK_OF_YEAR -> emit(
                    CaseResult.Success(
                        createAppInfoForWeek(
                            packageName,
                            calendarScreenTime,
                            checkPackage.values.first()
                        )
                    )
                )
            }
        } else {
            emit(CaseResult.Success(AppInfo()))
        }
    }

    private fun initTotalBeginEndTime(calendarScreenTime: CalendarScreenTime) {
        totalBeginTime = Calendar.getInstance()
        totalBeginTime.set(Calendar.HOUR_OF_DAY, 0)
        totalBeginTime.set(Calendar.MINUTE, 0)
        totalBeginTime.set(Calendar.SECOND, 0)
        totalBeginTime.set(Calendar.MILLISECOND, 0)

        totalEndTime = Calendar.getInstance()
        totalEndTime.set(Calendar.HOUR_OF_DAY, 0)
        totalEndTime.set(Calendar.MINUTE, 0)
        totalEndTime.set(Calendar.SECOND, -1)
        totalEndTime.set(Calendar.MILLISECOND, 0)

        if (calendarScreenTime.dataType == Calendar.DATE) {
            totalBeginTime.add(calendarScreenTime.dataType, -calendarScreenTime.beginTime)
            totalEndTime.add(calendarScreenTime.dataType, -calendarScreenTime.endTime)
        } else {
            totalBeginTime.set(Calendar.DAY_OF_WEEK, totalBeginTime.firstDayOfWeek)
            totalBeginTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.beginTime)
            totalEndTime.set(Calendar.DAY_OF_WEEK, totalBeginTime.firstDayOfWeek)
            totalEndTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.endTime)
        }
    }

    private fun createAppInfoForDay(
        packageName: String,
        calendarScreenTime: CalendarScreenTime,
        usageStats: UsageStats
    ): AppInfo {
        hourAppInfo(packageName, calendarScreenTime)
        return AppInfo(
            nameApp = getAppLabel(packageName).toString(),
            icon = getAppIcon(packageName),
            listTime = listHour,
            lastLaunch = mapTimeToString(usageStats.lastTimeUsed),
            data = "0",
            totalTimeUsage = listHour.sum()
        )
    }

    private fun createAppInfoForWeek(
        packageName: String,
        calendarScreenTime: CalendarScreenTime,
        usageStats: UsageStats
    ): AppInfo {
        dayAppInfo(packageName, calendarScreenTime)
        return AppInfo(
            nameApp = getAppLabel(packageName).toString(),
            icon = getAppIcon(packageName),
            listTime = listDay,
            lastLaunch = mapTimeToString(usageStats.lastTimeUsed),
            data = "0",
            totalTimeUsage = listHour.sum()
        )
    }

    private fun hourAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        initHourBeginEndTime(calendarScreenTime)
        for (i in 0..23) {
            calculationHourTime(i)
            stats.queryAndAggregateUsageStats(
                hourBeginTime.timeInMillis,
                hourEndTime.timeInMillis
            ).values.filter { it.packageName == packageName }.forEach {
                listHour.add(mapTimeToMinutes(it.totalTimeInForeground))
            }
        }
    }

    private fun dayAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        initDayBeginEndTime(calendarScreenTime)
        for (i in 2..8) {
            calculateDayTime(i)
            stats.queryAndAggregateUsageStats(
                dayBeginTime.timeInMillis,
                dayEndTime.timeInMillis
            ).values.filter { it.packageName == packageName }.forEach {
                listDay.add(mapTimeToMinutes(it.totalTimeInForeground))
            }
        }
    }

    private fun mapTimeToMinutes(totalTimeInForeground: Long): Int {
        return (totalTimeInForeground / 60000).toInt()
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

    private fun getBatteryCharge(packageName: String) {}

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

    private fun calculationHourTime(hour: Int) {
        if (hour == 0) return
        hourBeginTime.set(Calendar.HOUR_OF_DAY, hour)
        hourEndTime.set(Calendar.HOUR_OF_DAY, hour + 1)
    }


    private fun initDayBeginEndTime(calendarScreenTime: CalendarScreenTime) {
        dayBeginTime = Calendar.getInstance()
        dayBeginTime.set(Calendar.DAY_OF_WEEK, dayBeginTime.firstDayOfWeek)
        dayBeginTime.set(Calendar.HOUR_OF_DAY, 0)
        dayBeginTime.set(Calendar.MINUTE, 0)
        dayBeginTime.set(Calendar.SECOND, 0)
        dayBeginTime.set(Calendar.MILLISECOND, 0)

        dayEndTime = Calendar.getInstance()
        dayEndTime.set(Calendar.DAY_OF_WEEK, dayBeginTime.firstDayOfWeek)
        dayEndTime.set(Calendar.HOUR_OF_DAY, 0)
        dayEndTime.set(Calendar.MINUTE, 0)
        dayEndTime.set(Calendar.SECOND, -1)
        dayEndTime.set(Calendar.MILLISECOND, 0)

        dayBeginTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.beginTime)
        dayEndTime.add(Calendar.WEEK_OF_YEAR, -calendarScreenTime.endTime)
    }

    private fun calculateDayTime(day: Int) {
        dayBeginTime.set(Calendar.DAY_OF_WEEK, day)
        if (day != 2) {
            dayEndTime.set(Calendar.DAY_OF_WEEK, day)
        } else {
            dayEndTime = dayBeginTime.clone() as Calendar
            dayEndTime.set(Calendar.DAY_OF_WEEK, day + 1)
            dayEndTime.set(Calendar.SECOND, -1)
        }
    }

    private fun mapTimeToString(time: Long): String {
        val hour = (time / (1000 * 60 * 60))
        val minutes = ((time / (1000 * 60)) % 60)
        val second = time / 1000
        return if (hour == 0L && minutes == 0L) {
            context.getString(R.string.D_second, second)
        } else {
            context.getString(R.string.D_hour_D_minutes, hour, minutes)
        }
    }

}