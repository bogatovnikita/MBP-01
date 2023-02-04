package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase
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
            else -> {}
        }

    }

    private fun selectTimePicker(isStart: Boolean) {
        updateState {
            it.copy(
                timeStartSelected = isStart,
                timeEndSelected =  !isStart,
            )
        }
    }

    private fun saveTime(time: Int) {
        viewModelScope.launch {
            if (screenState.value.timeStartSelected && screenState.value.endTime > time) {
                useCase.setStartTime(time)
            } else if (screenState.value.timeEndSelected && screenState.value.startTime < time) {
                useCase.setEndTime(time)
            } else {
                setEvent(DNDSettingsEvent.WrongFormat)
                return@launch
            }
            updateState {
                it.copy(
                    timeStartSelected = false,
                    timeEndSelected =  false,
                    startTime = useCase.getStartTime(),
                    endTime = useCase.getEndTime(),
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