package yin_kio.acceleration.presentation.selectable_acceleration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCases


class SelectableAccelerationViewModel(
    private val useCases: SelectableAccelerationUseCases,
    private val coroutineScope: CoroutineScope
) : MutableSelectableAccelerationViewModel,
        ObservableViewModel,
        SelectableAccelerationUseCases by useCases
{

    private val _flow = MutableStateFlow(ScreenState())
    override val flow: Flow<ScreenState> = _flow.asStateFlow()

    private val _commandsFlow = MutableSharedFlow<Any>()
    override val commandsFlow = _commandsFlow.asSharedFlow()

    init {
        updateList()
    }

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
    override fun setApps(apps: List<App>){
        _flow.value = _flow.value.copy(apps = apps)
    }

    override fun setSelectedApps(selectedApps: List<App>) {
        _flow.value = _flow.value.copy(selectedApps = selectedApps)
    }

    override fun updateApps() {
        coroutineScope.launch {
            _commandsFlow.emit(Any())
        }
    }
}