package com.hedgehog.domain.usacase

import android.app.usage.UsageStats
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.repository.ScreenTimeDataRepository
import com.hedgehog.domain.wrapper.CaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScreenTimeDataUseCase @Inject constructor(private val screenTimeDataRepository: ScreenTimeDataRepository) {

    fun invoke(calendarScreenTime: CalendarScreenTime): Flow<CaseResult<List<UsageStats>, String>> =
        screenTimeDataRepository.getScreenTimeData(calendarScreenTime)
            .catch { e -> e.printStackTrace() }
            .cancellable()
            .flowOn(Dispatchers.IO)

}