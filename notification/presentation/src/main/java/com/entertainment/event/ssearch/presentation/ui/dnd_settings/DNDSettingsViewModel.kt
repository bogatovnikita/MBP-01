package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase
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

    fun initState() {
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

}