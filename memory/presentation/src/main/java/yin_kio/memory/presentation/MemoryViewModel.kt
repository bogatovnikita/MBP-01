package yin_kio.memory.presentation

import kotlinx.coroutines.flow.SharedFlow

interface MemoryViewModel {

    val flow: SharedFlow<UiState>

}