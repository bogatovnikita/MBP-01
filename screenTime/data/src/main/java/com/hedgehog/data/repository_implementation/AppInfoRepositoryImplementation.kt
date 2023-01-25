package com.hedgehog.data.repository_implementation

import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
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
    private var listDay: MutableList<Long> = mutableListOf()


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
                Calendar.DATE -> {
                    hourAppInfo(packageName, calendarScreenTime)
                    getBatteryCharge(packageName)
                }
                Calendar.WEEK_OF_YEAR -> weekAppInfo()
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

    private fun getBatteryCharge(packageName: String) {
        val z = context.packageManager.getPackageInfo(packageName, 0).applicationInfo.dataDir
        val t = File(z)
        Log.e("pie", "Z=: $z")
        Log.e("pie", "T=: $t")

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

    private fun calculationHourTime(hour: Int) {
        if (hour == 0) return
        hourBeginTime.set(Calendar.HOUR_OF_DAY, hour)
        hourEndTime.set(Calendar.HOUR_OF_DAY, hour + 1)
    }


    private fun weekAppInfo() {

    }


    private fun initDayBeginEndTime() {

    }

}