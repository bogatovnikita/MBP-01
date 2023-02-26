package com.entertainment.event.ssearch.presentation.ui.device_info

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.models.DeviceFunction
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
            updateState {
                it.copy(
                    deviceInfo = deviceInfoUseCase.getDeviceInfo()
                )
            }
        }
    }

    fun expandOrCollapseList(deviceFunction: DeviceFunction) {

    }

}