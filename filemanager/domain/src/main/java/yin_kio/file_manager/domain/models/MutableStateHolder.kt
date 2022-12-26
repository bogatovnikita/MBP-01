package yin_kio.file_manager.domain.models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MutableStateHolder : StateHolder {

    private val _state = MutableState()
    private val _flow = MutableStateFlow<State>(_state)


    override val flow: StateFlow<State>
        get() = _flow.asStateFlow()

    override val value: State
        get() = flow.value

    fun update(state: State){
        _flow.value = state
    }


}