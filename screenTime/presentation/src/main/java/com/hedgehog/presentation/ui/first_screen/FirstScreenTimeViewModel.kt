package com.hedgehog.presentation.ui.first_screen

import androidx.lifecycle.viewModelScope
import com.hedgehog.domain.usacase.GetScreenTimeDataUseCase
import com.hedgehog.domain.wrapper.CaseResult
import com.hedgehog.presentation.base.BaseViewModel
import com.hedgehog.presentation.di.MultiChoice
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.AppScreenTimeListItems
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.multichoice.MultiChoiceHandler
import com.hedgehog.presentation.multichoice.MultiChoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
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
                            it.copy(isErrorLoading = true)
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
                }
            )
        }
        initChoiceHandler()
    }

    private fun initChoiceHandler() {
        val flow = flowOf(_screenState.value.listDataScreenTime)
        viewModelScope.launch {
            multiChoiceHandler.setItemsFlow(viewModelScope, flow)
            val combinedFlow = combine(
                flow,
                multiChoiceHandler.listen(),
                ::merge
            )

            combinedFlow.collectLatest {
                updateState { state ->
                    state.copy(
                        appScreenTimeListItems = it.appScreenTimeListItems,
                        isLoading = true,
                        reverseListAppScreenTime = false
                    )
                }
            }
        }
    }

    private fun merge(
        appScreenTimeList: List<AppScreenTime>,
        multiChoiceState: MultiChoiceState<AppScreenTime>
    ): FirstScreenTimeState {
        return FirstScreenTimeState(
            appScreenTimeListItems = appScreenTimeList.map { app ->
                AppScreenTimeListItems(
                    app,
                    multiChoiceState.isChecked(app),
                    _screenState.value.selectionMode
                )
            },
            totalCount = appScreenTimeList.size,
            totalCheckedCount = multiChoiceState.totalCheckedCount
        )
    }

    fun toggleSelection(app: AppScreenTimeListItems) {
        multiChoiceHandler.toggle(app.originAppScreenTime)
    }

    fun choiceDay() {
        updateState {
            it.copy(
                choiceDay = true,
                choiceWeek = false,
                reverseListAppScreenTime = false
            )
        }
    }

    fun choiceWeek() {
        updateState {
            it.copy(
                choiceDay = false,
                choiceWeek = true,
                reverseListAppScreenTime = false
            )
        }
    }

    fun reverseList() {
        updateState {
            it.copy(
                appScreenTimeListItems = it.appScreenTimeListItems.reversed(),
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
//        initChoiceHandler()
    }
}