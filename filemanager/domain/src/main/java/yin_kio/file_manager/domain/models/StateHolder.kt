package yin_kio.file_manager.domain.models

import kotlinx.coroutines.flow.StateFlow

interface StateHolder {
    val flow : StateFlow<State>
    val value: State
}