package com.hedgehog.presentation.ui.first_screen

import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.usacase.GetScreenTimeDataUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.CalendarScreenTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FirstScreenTimeViewModel @Inject constructor(
    private val getScreenTimeDataUseCase: GetScreenTimeDataUseCase
) : BaseViewModel<FirstScreenTimeState>(FirstScreenTimeState()) {
    var beginTime = 0
    var endTime = -1
    var calendar: Calendar = Calendar.getInstance()
    var secondCalendar: Calendar = Calendar.getInstance()

    fun getListTimeScreenData(calendarScreenTime: CalendarScreenTime) {
        updateState { it.copy(isLoading = false) }
        viewModelScope.launch {
            getScreenTimeDataUseCase.invoke(
                com.hedgehog.domain.models.CalendarScreenTime(
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

    private fun onSuccess(result: CaseResult.Success<List<com.hedgehog.domain.models.AppScreenTime>>) {
        if (_screenState.value.reverseListAppScreenTime) {
            updateState {
                it.copy(
                    listDataScreenTime = mapToAppScreenTime(result).reversed(),
                    isLoading = true
                )
            }
        } else {
            updateState {
                it.copy(
                    listDataScreenTime = mapToAppScreenTime(result),
                    isLoading = true
                )
            }
        }
        val size = _screenState.value.listDataScreenTime.filter { it.isItSystemApp }.size
        updateState { it.copy(systemCheckedCount = size) }
    }

    private fun mapToAppScreenTime(result: CaseResult.Success<List<com.hedgehog.domain.models.AppScreenTime>>) =
        result.response.map { usageState ->
            AppScreenTime(
                packageName = usageState.packageName,
                name = usageState.name,
                time = usageState.time,
                icon = usageState.icon,
                isItSystemApp = usageState.isItSystemApp
            )
        }

    fun choiceDay() {
        updateState {
            it.copy(
                choiceDay = true,
                choiceWeek = false
            )
        }
    }

    fun choiceWeek() {
        updateState {
            it.copy(
                choiceDay = false,
                choiceWeek = true
            )
        }
    }

    fun reverseList() {
        updateState {
            it.copy(
                listDataScreenTime = it.listDataScreenTime.reversed(),
                reverseListAppScreenTime = !it.reverseListAppScreenTime
            )
        }
    }

    fun selectedMode() = updateState { it.copy(selectionMode = !it.selectionMode) }


    fun toggleCheckBox(item: AppScreenTime) {
        var temp = _screenState.value.totalCheckedCount
        _screenState.value.listDataScreenTime.forEach {
            if (it.packageName == item.packageName) {
                it.isChecked = !it.isChecked
                if (it.isChecked) {
                    temp += 1
                } else {
                    temp -= 1
                }
            }
        }
        updateState {
            it.copy(
                listDataScreenTime = _screenState.value.listDataScreenTime,
                totalCheckedCount = temp
            )
        }
    }

    fun cleanToggleCheckBox() =
        updateState {
            it.copy(
                listDataScreenTime = mapToCleanToggleCheckBox(), totalCheckedCount = 0
            )
        }

    private fun mapToCleanToggleCheckBox() = _screenState.value.listDataScreenTime.map { item ->
        AppScreenTime(
            packageName = item.packageName,
            name = item.name,
            time = item.time,
            icon = item.icon,
            isItSystemApp = item.isItSystemApp,
            isChecked = false
        )
    }

    fun selectAll() {
        updateState {
            it.copy(
                listDataScreenTime = mapToSelectAll(),
                totalCheckedCount = _screenState.value.listDataScreenTime.size - _screenState.value.systemCheckedCount
            )
        }
    }

    private fun mapToSelectAll() = _screenState.value.listDataScreenTime.map { item ->
        AppScreenTime(
            packageName = item.packageName,
            name = item.name,
            time = item.time,
            icon = item.icon,
            isItSystemApp = item.isItSystemApp,
            isChecked = !item.isItSystemApp
        )
    }
}