package com.hedgehog.domain.usacase

import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.wrapper.CaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(private val appInfoRepository: AppInfoRepository) {
    fun invoke(
        packageName: String,
        calendarScreenTime: CalendarScreenTime
    ): Flow<CaseResult<AppInfo, String>> =
        appInfoRepository.getAppInfo(packageName, calendarScreenTime)
            .catch { e -> e.printStackTrace() }
            .cancellable()
            .flowOn(Dispatchers.IO)
}