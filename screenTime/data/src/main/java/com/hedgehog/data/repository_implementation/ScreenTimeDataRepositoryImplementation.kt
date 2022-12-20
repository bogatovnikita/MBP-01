package com.hedgehog.data.repository_implementation

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
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

    override fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>> =
        flow {
            val cal = Calendar.getInstance()
            cal.add(calendarScreenTime.dataType, calendarScreenTime.dataCount)
            stats = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val statsList = stats.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                cal.timeInMillis,
                System.currentTimeMillis()
            )
            val appScreenList = statsList.map {
                appScreenTime(it)
            }.filter {
                it.time > 0
            }.sortedBy {
                it.time
            }
            try {
                emit(CaseResult.Success(appScreenList))
            } catch (e: Exception) {
                CaseResult.Failure(e.toString())
            }
        }

    private fun appScreenTime(it: UsageStats) = AppScreenTime(
        name = context.packageManager.getApplicationInfo(it.packageName, 0)
            .loadLabel(context.packageManager).toString(),
        time = it.totalTimeInForeground,
        icon = context.packageManager.getApplicationInfo(it.packageName, 0).icon
    )
}