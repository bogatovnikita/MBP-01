package yin_kio.memory.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import yin_kio.memory.domain.MemoryUseCases

class MemoryViewModelImpl(
    private val useCases: MemoryUseCases
) : MemoryViewModel, MutableMemoryViewModel {

    private var uiState = UiState()

    private val _flow = MutableSharedFlow<UiState>(replay = 1)
    override val flow = _flow.asSharedFlow()


    init {
        update()
    }

    override suspend fun setRamState(ramState: MemoryState) {
        uiState = uiState.copy(ram = ramState)
    }

    override suspend fun setStorageState(storageState: MemoryState) {
        uiState = uiState.copy(storage = storageState)
    }

    override suspend fun notify() {
        _flow.emit(uiState)
    }

    private fun update(){
        useCases.update()
    }
}