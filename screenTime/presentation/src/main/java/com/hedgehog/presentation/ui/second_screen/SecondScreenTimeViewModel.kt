package com.hedgehog.presentation.ui.second_screen

import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.models.AppInfo
import com.hedgehog.domain.usacase.GetAppInfoUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
import com.hedgehog.presentation.models.CalendarScreenTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SecondScreenTimeViewModel @Inject constructor(
    private val appInfoUseCase: GetAppInfoUseCase
) : BaseViewModel<SecondScreenTimeState>(SecondScreenTimeState()) {
    var beginTime = 0
    var endTime = -1
    var calendar: Calendar = Calendar.getInstance()
    var secondCalendar: Calendar = Calendar.getInstance()

    fun getAppInfo(packageName: String, calendarScreenTime: CalendarScreenTime) {
        updateState { it.copy(isLoading = false) }
        viewModelScope.launch {
            appInfoUseCase.invoke(
                packageName, com.hedgehog.domain.models.CalendarScreenTime(
                    dataType = calendarScreenTime.dataType,
                    beginTime = calendarScreenTime.beginTime,
                    endTime = calendarScreenTime.endTime
                )
            ).collect { result ->
                when (result) {
                    is CaseResult.Success -> onSuccess(result)
                    is CaseResult.Failure -> {
                        updateState {
                            it.copy(isErrorLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun onSuccess(result: CaseResult.Success<AppInfo>) {
        updateState {
            it.copy(
                appInfo = com.hedgehog.presentation.models.AppInfo(
                    nameApp = result.response.nameApp,
                    icon = result.response.icon,
                    listTime = result.response.listTime,
                    lastLaunch = result.response.lastLaunch,
                    data = result.response.data,
                    totalTimeUsage = result.response.totalTimeUsage,
                    isSystemApp = result.response.isSystemApp
                ), isLoading = true
            )
        }
    }

    fun choiceDay() {
        updateState {
            it.copy(
                choiceDay = true,
                choiceWeek = false,
                isLoading = false
            )
        }
    }

    fun choiceWeek() {
        updateState {
            it.copy(
                choiceDay = false,
                choiceWeek = true,
                isLoading = false
            )
        }
    }
}