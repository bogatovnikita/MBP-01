package com.battery_saving.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.battery_saving.presentation.room.BatteryChargeDAO
import com.battery_saving.presentation.utils.GetBatteryChargePercent
import com.battery_saving.presentation.utils.GetBatteryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BatterySavingViewModel @Inject constructor(
    private val getBatteryChargePercent: GetBatteryChargePercent,
    private val getBatteryWorker: GetBatteryWorker,
    private val database: BatteryChargeDAO
) : ViewModel() {

    private val _state: MutableStateFlow<BatterySavingState> =
        MutableStateFlow(BatterySavingState())
    val state = _state.asStateFlow()

    init {
        getBatteryPercent()
        checkDatabase()
    }

    fun getBatteryPercent(): Job {
        return viewModelScope.launch {
            while (isActive) {
                getBatteryChargePercent.getBatteryChargePercent().collect {
                    _state.value = state.value.copy(
                        batteryChargePercent = it
                    )
                }
                delay(5000)
            }
        }
    }

    private fun checkDatabase() {
        getBatteryWorker.getWorker()
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = state.value.copy(
                listConsumptionPercent = database.getDatabase()
            )
        }
    }
}