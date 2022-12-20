package com.hedgehog.domain.repository

import com.hedgehog.domain.models.AppScreenTime
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.wrapper.CaseResult
import kotlinx.coroutines.flow.Flow

interface ScreenTimeDataRepository {
    fun getScreenTimeData(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<AppScreenTime>, String>>
}