package yin_kio.acceleration.presentation.acceleration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCases

class AccelerationViewModel(
    private val useCases: AccelerationUseCases
) : MutableAccelerationViewModel,
    ObservableAccelerationViewModel,
    AccelerationUseCases by useCases {

    private val _flow = MutableStateFlow(ScreenState())

    override val flow: Flow<ScreenState> = _flow.asStateFlow()

    override fun setRamInfo(ramInfo: RamInfo) {
        _flow.value = _flow.value.copy(ramInfo = ramInfo)
    }

    override fun setAppsState(appsState: AppsState) {
        _flow.value = _flow.value.copy(appsState = appsState)
    }
}