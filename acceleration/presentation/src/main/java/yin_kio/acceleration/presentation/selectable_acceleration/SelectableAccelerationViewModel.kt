package yin_kio.acceleration.presentation.selectable_acceleration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCases


class SelectableAccelerationViewModel(
    private val useCases: SelectableAccelerationUseCases
) : MutableSelectableAccelerationViewModel,
        ObservableViewModel,
        SelectableAccelerationUseCases by useCases
{

    private val _flow = MutableStateFlow(ScreenState())
    override val flow: Flow<ScreenState> = _flow.asStateFlow()

    override fun setAppSelected(packageName: String, isSelected: Boolean){}
    override fun setButtonBgRes(resId: Int){
        _flow.value = _flow.value.copy(buttonBgRes = resId)
    }
    override fun setAllSelected(isAllSelected: Boolean){
        _flow.value = _flow.value.copy(isAllSelected = isAllSelected)
    }
    override fun setProgressVisible(isVisible: Boolean){
        _flow.value = _flow.value.copy(isProgressVisible = isVisible)
    }
    override fun setListVisible(isVisible: Boolean){
        _flow.value = _flow.value.copy(isListVisible = isVisible)
    }
    override fun setApps(apps: List<String>){
        _flow.value = _flow.value.copy(apps = apps)
    }

}