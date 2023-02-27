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
    }

    private fun getDeviceInfo() {
        viewModelScope.launch {
            val mainList = deviceInfoUseCase.getDeviceInfo()
            updateState {
                it.copy(
                    showedDeviceInfo = preparingList(mainList),
                    mainDeviceInfo = mainList
                )
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

}