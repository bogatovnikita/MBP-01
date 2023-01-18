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
                    listDataScreenTime = result.response.map { usageState ->
                        AppScreenTime(
                            packageName = usageState.packageName,
                            name = usageState.name,
                            time = usageState.time,
                            icon = usageState.icon,
                            isItSystemApp = usageState.isItSystemApp
                        )
                    }.reversed(), isLoading = true, listIsEmpty = false
                )
            }
        } else {
            updateState {
                it.copy(
                    listDataScreenTime = result.response.map { usageState ->
                        AppScreenTime(
                            packageName = usageState.packageName,
                            name = usageState.name,
                            time = usageState.time,
                            icon = usageState.icon,
                            isItSystemApp = usageState.isItSystemApp
                        )
                    }, isLoading = true, listIsEmpty = false
                )
            }
        }
        if (_screenState.value.listDataScreenTime.isEmpty()) {
            updateState {
                it.copy(
                    listIsEmpty = true
                )
            }
        }
        val size = _screenState.value.listDataScreenTime.filter { it.isItSystemApp }.size
        updateState {
            it.copy(
                systemCheckedCount = size
            )
        }
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

    fun selectedMode() {
        updateState {
            it.copy(
                selectionMode = !it.selectionMode
            )
        }
    }

    fun toggleCheckBox(item: AppScreenTime) {
        var temp = _screenState.value.totalCheckedCount
        _screenState.value.listDataScreenTime.forEach {
            if (it.packageName == item.packageName) {
                it.isChecked = !it.isChecked
                if (it.isChecked) {
                    temp -= 1
                } else {
                    temp += 1
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

    fun cleanToggleCheckBox() {
        updateState {
            it.copy(
                listDataScreenTime = _screenState.value.listDataScreenTime.map { item ->
                    AppScreenTime(
                        packageName = item.packageName,
                        name = item.name,
                        time = item.time,
                        icon = item.icon,
                        isItSystemApp = item.isItSystemApp,
                        isChecked = false
                    )
                }, totalCheckedCount = 0
            )
        }
    }

    fun selectAll() {
        updateState {
            it.copy(
                listDataScreenTime = _screenState.value.listDataScreenTime.map { item ->
                    AppScreenTime(
                        packageName = item.packageName,
                        name = item.name,
                        time = item.time,
                        icon = item.icon,
                        isItSystemApp = item.isItSystemApp,
                        isChecked = !item.isItSystemApp
                    )
                },
                totalCheckedCount = _screenState.value.listDataScreenTime.size - _screenState.value.systemCheckedCount
            )
        }
    }
}