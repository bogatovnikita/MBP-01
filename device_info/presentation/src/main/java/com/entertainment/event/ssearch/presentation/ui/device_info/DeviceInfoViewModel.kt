package com.entertainment.event.ssearch.presentation.ui.device_info

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.models.DeviceFunction
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import com.entertainment.event.ssearch.domain.use_cases.DeviceInfoUseCase
import com.entertainment.event.ssearch.presentation.models.DeviceInfoScreenState
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceInfoViewModel @Inject constructor(
    private val deviceInfoUseCase: DeviceInfoUseCase,
) : BaseViewModel<DeviceInfoScreenState>(DeviceInfoScreenState()) {

    init {
        getDeviceInfo()
        deviceInfoUseCase.registerBatteryReceiver()
    }

    private fun getDeviceInfo() {
        viewModelScope.launch {
            deviceInfoUseCase.getDeviceInfo().collect { mainList ->
                val mergedList = mergeLists(mainList)
                updateState {
                    it.copy(
                        showedDeviceInfo = preparingList(mergedList),
                        mainDeviceInfo = mergedList
                    )
                }
            }
        }
    }

    fun startObserve() {
        deviceInfoUseCase.startObserve()
    }

    fun stopObserve() {
        deviceInfoUseCase.stopObserve()
    }

    private fun mergeLists(newList: List<DeviceFunctionGroup>): List<DeviceFunctionGroup> {
        val oldList = screenState.value.mainDeviceInfo
        return if (oldList.isEmpty()) {
            newList
        } else {
            oldList.mapIndexed { index, funGroup ->
                DeviceFunctionGroup(parentFun = funGroup.parentFun, listFun = newList[index].listFun)
            }
        }
    }

    private fun preparingList(deviceInfo: List<DeviceFunctionGroup>): List<DeviceFunction> {
        val newList = mutableListOf<DeviceFunction>()
        deviceInfo.forEach { deviceFunction ->
            newList.add(deviceFunction.parentFun)
            if (deviceFunction.parentFun.isExpanded) {
                newList.addAll(deviceFunction.listFun)
            }
        }
        return newList
    }

    fun expandOrCollapseList(deviceFun: ParentFun) {
        val newList = screenState.value.mainDeviceInfo.toMutableList()
        val index = newList.indexOfFirst { it.parentFun == deviceFun }
        newList[index] = DeviceFunctionGroup(deviceFun.copy(isExpanded = !deviceFun.isExpanded), listFun = newList[index].listFun)
        updateState {
            it.copy(
                showedDeviceInfo = preparingList(newList),
                mainDeviceInfo = newList
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        deviceInfoUseCase.unregisterBatteryReceiver()
    }
}