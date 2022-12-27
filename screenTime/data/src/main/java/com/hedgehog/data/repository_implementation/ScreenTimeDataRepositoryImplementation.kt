package com.hedgehog.data.repository_implementation

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.hedgehog.data.R
import com.hedgehog.domain.models.AppScreenTime
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.ScreenTimeDataRepository
import com.hedgehog.domain.wrapper.CaseResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScreenTimeDataRepositoryImplementation @Inject constructor(@ApplicationContext val context: Context) :
    ScreenTimeDataRepository {

    private lateinit var stats: UsageStatsManager
    private val MINUTE = 60000

    override fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>> =
        flow {
            try {
                val cal = Calendar.getInstance()
                cal.add(calendarScreenTime.dataType, calendarScreenTime.dataCount)
                stats = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                val statsList = stats.queryAndAggregateUsageStats(
                    cal.timeInMillis,
                    System.currentTimeMillis()
                ).values.toList()

                val appScreenList = statsList.filter {
                    it.totalTimeInForeground > MINUTE
                }.sortedByDescending {
                    it.totalTimeInForeground
                }.map {
                    appScreenTime(it)
                }
                emit(CaseResult.Success(appScreenList))
            } catch (e: Exception) {
                CaseResult.Failure(e.toString())
            }
        }

    private fun appScreenTime(it: UsageStats) = AppScreenTime(
        name = getAppLabel(it).toString(),
        time = mapTimeToString(it.totalTimeInForeground),
        icon = getAppIcon(it)
    )

    private fun mapTimeToString(time: Long): String {
        val hour = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
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

}