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
import java.util.*
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
            mapToPercent()
        }
    }

    private fun mapToPercent() {
        val beginCalendar = Calendar.getInstance()
        beginCalendar.add(Calendar.HOUR, -1)
        beginCalendar.set(Calendar.MINUTE, 0)
        beginCalendar.set(Calendar.SECOND, 0)
        val beginTime = beginCalendar.timeInMillis

        val endCalendar = Calendar.getInstance()
        endCalendar.set(Calendar.MINUTE, 0)
        endCalendar.set(Calendar.SECOND, 0)
        val endTime = endCalendar.timeInMillis
        val filterList =
            _state.value.listConsumptionPercent.filter { it.time in (beginTime + 1) until endTime }
        if (filterList.isNotEmpty()) {
            val firstValue = filterList.first().percentCharge
            val lastValue = filterList.last().percentCharge
            when {
                filterList.size < 2 || lastValue > firstValue -> saveConsumptionPercent(-1)
                lastValue == firstValue -> saveConsumptionPercent(0)
                lastValue < firstValue -> saveConsumptionPercent(firstValue - lastValue)
            }
        } else {
            saveConsumptionPercent(-1)
        }
    }

    private fun saveConsumptionPercent(value: Int) {
        _state.value = state.value.copy(
            consumptionPercent = value,
            isLoading = true
        )
    }

}