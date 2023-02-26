package com.entertainment.event.ssearch.presentation.ui.device_info

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.models.DeviceFunction
import com.entertainment.event.ssearch.domain.models.ParentFun
import com.entertainment.event.ssearch.domain.use_cases.DeviceInfoUseCase
import com.entertainment.event.ssearch.domain.utility.PARENT
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
            Log.e("!!!", "${deviceInfoUseCase.getDeviceInfo()}")
            updateState {
                it.copy(
                    deviceInfo = preparingList(deviceInfoUseCase.getDeviceInfo())
                )
            }
        }
    }

    private fun preparingList(deviceInfo: List<DeviceFunction>): List<DeviceFunction> {
        val parentIndexes = mutableListOf<Int>()
        val newList = mutableListOf<DeviceFunction>()
        deviceInfo.forEachIndexed { index, deviceFunction ->
            if (deviceFunction.type == PARENT) parentIndexes.add(index)
        }
        parentIndexes.add(6)
        parentIndexes.sort()
        deviceInfo.forEachIndexed { index, deviceFunction ->
            if (index < parentIndexes[1] && (deviceInfo[parentIndexes[0]] as ParentFun).isExpanded || parentIndexes[0] == index) {
                newList.add(deviceFunction)
            }
        }
        return newList
    }

    fun expandOrCollapseList(deviceFun: ParentFun) {
        viewModelScope.launch {
            Log.e("!!!", "${deviceFun}")
            val newList = deviceInfoUseCase.getDeviceInfo().toMutableList()
            val index = newList.indexOf(deviceFun.copy(isExpanded = false))
            newList[index] = deviceFun.copy(isExpanded = !deviceFun.isExpanded)
            Log.e("!!!", "${newList[index]}")
            Log.e("!!!", "$newList")
            updateState {
                it.copy(
                    deviceInfo = preparingList(newList)
                )
            }
        }
    }

}