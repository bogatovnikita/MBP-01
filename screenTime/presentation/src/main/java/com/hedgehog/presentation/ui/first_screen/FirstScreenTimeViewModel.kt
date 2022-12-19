package com.hedgehog.presentation.ui.first_screen

import android.app.usage.UsageStats
import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.models.CalendarScreenTime
import com.hedgehog.domain.usacase.GetScreenTimeDataUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
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
                calendarScreenTime
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

    private fun onSuccess(result: CaseResult.Success<List<UsageStats>>) {
        val tempList = result.response.filter {
            it.totalTimeInForeground > 0
        }
        updateState {
            it.copy(
                listDataScreenTime = tempList
            )
        }
    }
}