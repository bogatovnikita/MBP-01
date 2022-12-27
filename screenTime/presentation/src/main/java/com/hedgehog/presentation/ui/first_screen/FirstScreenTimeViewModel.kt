package com.hedgehog.presentation.ui.first_screen

import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.usacase.GetScreenTimeDataUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.CalendarScreenTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstScreenTimeViewModel @Inject constructor(
    private val getScreenTimeDataUseCase: GetScreenTimeDataUseCase
) : BaseViewModel<FirstScreenTimeState>(FirstScreenTimeState()) {

    fun getListTimeScreenData(calendarScreenTime: CalendarScreenTime) {
        viewModelScope.launch {
            getScreenTimeDataUseCase.invoke(
                com.hedgehog.domain.models.CalendarScreenTime(
                    dataType = calendarScreenTime.dataType,
                    dataCount = calendarScreenTime.dataCount
                )
            ).collect { result ->
                when (result) {
                    is CaseResult.Success -> {
                        onSuccess(result)
                    }
                    is CaseResult.Failure -> {
                        updateState {
                            it.copy(
                                isErrorLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSuccess(result: CaseResult.Success<List<com.hedgehog.domain.models.AppScreenTime>>) {
        updateState {
            it.copy(
                listDataScreenTime = result.response.map { usageState ->
                    AppScreenTime(
                        name = usageState.name,
                        time = usageState.time,
                        icon = usageState.icon
                    )
                }, isLoading = true
            )
        }
    }
}