package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase
import com.entertainment.event.ssearch.presentation.extensions.toHours
import com.entertainment.event.ssearch.presentation.extensions.toMinutes
import com.entertainment.event.ssearch.presentation.models.DNDSettingsEvent
import com.entertainment.event.ssearch.presentation.models.DNDSettingsState
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DNDSettingsViewModel @Inject constructor(
    private val useCase: DNDSettingsUseCase,
) : BaseViewModel<DNDSettingsState>(DNDSettingsState()) {

    init {
        initState()
        checkAlarmOnNextDay()
    }

    private fun initState() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isAutoModeSwitched = useCase.isAutoModeSwitched(),
                    selectedDays = useCase.getSelectedDays(),
                    startTime = useCase.getStartTime(),
                    endTime = useCase.getEndTime()
                )
            }
        }
    }

    fun obtainEvent(event: DNDSettingsEvent) {
        when (event) {
            is DNDSettingsEvent.SetAutoModeDND -> setAutoModeDND(event.isSwitched)
            is DNDSettingsEvent.SelectStartTime -> selectTimePicker(true)
            is DNDSettingsEvent.SelectEndTime -> selectTimePicker(false)
            is DNDSettingsEvent.SaveTime -> saveTime(event.time)
            is DNDSettingsEvent.Default -> setEvent(event)
            is DNDSettingsEvent.UpdateSelectedDayList -> setAlarms(event.days)
        }
    }

    private fun setAlarms(days: List<Int>) {
        updateState {
            it.copy(
                selectedDays = days
            )
        }
        useCase.setRepeatAlarmStart(
            screenState.value.startTime.toHours(),
            screenState.value.startTime.toMinutes(),
            days
        )
        useCase.setRepeatAlarmEnd(
            screenState.value.endTime.toHours(),
            screenState.value.endTime.toMinutes(),
            days,
            screenState.value.isAlarmOnNextDay
        )
    }

    private fun selectTimePicker(isStart: Boolean) {
        updateState {
            it.copy(
                timeStartSelected = isStart,
                timeEndSelected = !isStart,
            )
        }
    }

    private fun saveTime(time: Int) {
        viewModelScope.launch {
            var isAlarmOnNextDay = false
            val hours = time.toHours()
            val minutes = time.toMinutes()
            with(screenState.value) {
                if (timeStartSelected && endTime > time) {
                    useCase.setStartTime(time)
                    useCase.setRepeatAlarmStart(hours, minutes, selectedDays)
                } else if (timeEndSelected && startTime < time) {
                    useCase.setEndTime(time)
                    useCase.setRepeatAlarmEnd(hours, minutes, selectedDays, isAlarmOnNextDay)
                } else if (timeStartSelected) {
                    isAlarmOnNextDay = true
                    useCase.setStartTime(time)
                    useCase.setRepeatAlarmStart(hours, minutes, selectedDays)
                    useCase.setRepeatAlarmEnd(
                        endTime.toHours(),
                        endTime.toMinutes(),
                        selectedDays,
                        isAlarmOnNextDay
                    )
                } else if (timeEndSelected) {
                    isAlarmOnNextDay = true
                    useCase.setEndTime(time)
                    useCase.setRepeatAlarmEnd(hours, minutes, selectedDays, isAlarmOnNextDay)
                } else {
                }
            }
            updateState {
                it.copy(
                    isAlarmOnNextDay = isAlarmOnNextDay,
                    timeStartSelected = false,
                    timeEndSelected = false,
                    startTime = useCase.getStartTime(),
                    endTime = useCase.getEndTime(),
                )
            }
        }
    }

    private fun checkAlarmOnNextDay() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isAlarmOnNextDay = useCase.getEndTime() < useCase.getStartTime(),
                )
            }
        }
    }

    private fun setAutoModeDND(isSwitched: Boolean) {
        viewModelScope.launch {
            useCase.setAutoModeDND(isSwitched)
            updateState {
                it.copy(
                    isAutoModeSwitched = isSwitched
                )
            }
            with(screenState.value) {
                if (isSwitched) {
                    useCase.setRepeatAlarmStart(
                        startTime.toHours(),
                        startTime.toMinutes(),
                        selectedDays
                    )
                    useCase.setRepeatAlarmEnd(
                        endTime.toHours(),
                        endTime.toMinutes(),
                        selectedDays,
                        isAlarmOnNextDay
                    )
                } else {
                    useCase.setRepeatAlarmStart(
                        startTime.toHours(),
                        startTime.toMinutes(),
                        emptyList()
                    )
                    useCase.setRepeatAlarmEnd(
                        endTime.toHours(),
                        endTime.toMinutes(),
                        emptyList(),
                        isAlarmOnNextDay
                    )
                }
            }
        }
    }

    private fun setEvent(event: DNDSettingsEvent) {
        updateState {
            it.copy(
                event = event
            )
        }
    }

}