package com.hedgehog.presentation.ui.first_screen

import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.usacase.GetScreenTimeDataUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
import com.hedgehog.presentation.di.MultiChoice
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.multichoice.MultiChoiceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstScreenTimeViewModel @Inject constructor(
    private val getScreenTimeDataUseCase: GetScreenTimeDataUseCase,
    @MultiChoice private val multiChoiceHandler: MultiChoiceHandler<AppScreenTime>
) : BaseViewModel<FirstScreenTimeState>(FirstScreenTimeState()) {

    fun getListTimeScreenData(calendarScreenTime: CalendarScreenTime) {
        viewModelScope.launch {
            getScreenTimeDataUseCase.invoke(
                com.hedgehog.domain.models.CalendarScreenTime(
                    dataType = calendarScreenTime.dataType,
                    beginTime = calendarScreenTime.beginTime,
                    endTime = calendarScreenTime.endTime
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

    fun choiceDay() {
        updateState {
            it.copy(choiceDay = true, choiceWeek = false)
        }
    }

    fun choiceWeek() {
        updateState {
            it.copy(choiceDay = false, choiceWeek = true)
        }
    }
}