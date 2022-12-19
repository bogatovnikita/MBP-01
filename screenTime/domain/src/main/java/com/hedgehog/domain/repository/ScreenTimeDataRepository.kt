package com.hedgehog.domain.repository

import android.app.usage.UsageStats
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.wrapper.CaseResult
import kotlinx.coroutines.flow.Flow

interface ScreenTimeDataRepository {
    fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<UsageStats>, String>>
}