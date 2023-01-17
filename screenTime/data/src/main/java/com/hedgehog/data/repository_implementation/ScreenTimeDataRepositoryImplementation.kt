package com.hedgehog.data.repository_implementation

import android.app.usage.UsageStats
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

    private lateinit var stats: UsageStatsManager
    private val minute = 60000
    private var appScreenList: MutableList<AppScreenTime> = mutableListOf()
    private lateinit var beginTime: Calendar
    private lateinit var endTime: Calendar

    override fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>> =
        flow {
            try {
                initBeginEndTime(calendarScreenTime)
                appScreenList.clear()
                stats = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                val statsList = stats.queryAndAggregateUsageStats(
                    beginTime.timeInMillis, endTime.timeInMillis
                ).values.toMutableList()
                appScreenList = statsList.filter {
                    it.totalTimeInForeground > minute && context.isPackageExist(it.packageName)
                }.sortedByDescending {
                    it.totalTimeInForeground
                }.map {
                    appScreenTime(it)
                }.toMutableList()
                statsList.clear()
                emit(CaseResult.Success(appScreenList))
            } catch (e: Exception) {
                CaseResult.Failure(e.toString())
            }
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

    private fun appScreenTime(it: UsageStats) = AppScreenTime(
        packageName = it.packageName,
        name = getAppLabel(it).toString(),
        time = mapTimeToString(it.totalTimeInForeground),
        icon = getAppIcon(it),
        isItSystemApp = checkIsSystemApp(it)
    )

    private fun mapTimeToString(time: Long): String {
        val hour = (time / (1000 * 60 * 60))
        val minutes = ((time / (1000 * 60)) % 60)
        return context.getString(R.string.D_hour_D_minutes, hour, minutes)
    }

    private fun getAppLabel(it: UsageStats) = try {
        context.packageManager.getApplicationInfo(it.packageName, 0)
            .loadLabel(context.packageManager)
    } catch (e: Exception) {
        it.packageName
    }

    private fun getAppIcon(it: UsageStats) = try {
        context.packageManager.getApplicationIcon(it.packageName)
    } catch (e: Exception) {
        null
    }

    private fun checkIsSystemApp(it: UsageStats) = try {
        context.packageManager.getApplicationInfo(
            it.packageName, 0
        ).flags and ApplicationInfo.FLAG_SYSTEM != 0 || context.packageManager.getApplicationInfo(
            it.packageName, 0
        ).flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
    } catch (e: Exception) {
        true
    }
}

fun Context.isPackageExist(target: String): Boolean {
    return packageManager.getInstalledApplications(0)
        .find { info -> info.packageName == target } != null
}