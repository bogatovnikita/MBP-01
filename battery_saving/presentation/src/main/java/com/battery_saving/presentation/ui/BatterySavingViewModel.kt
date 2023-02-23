package com.battery_saving.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.battery_saving.presentation.utils.GetBatteryChargePercent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatterySavingViewModel @Inject constructor(private val getBatteryChargePercent: GetBatteryChargePercent) :
    ViewModel() {

    private val _state: MutableStateFlow<BatterySavingState> =
        MutableStateFlow(BatterySavingState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getBatteryChargePercent.getBatteryChargePercent().collect() {
                _state.value = state.value.copy(
                    batteryChargePercent = it
                )
            }
        }
    }
}