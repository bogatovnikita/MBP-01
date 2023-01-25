package com.hedgehog.domain.repository

import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.wrapper.CaseResult
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {
    fun getAppInfo(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): Flow<CaseResult<AppInfo, String>>
}