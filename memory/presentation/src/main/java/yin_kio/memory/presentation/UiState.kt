package yin_kio.memory.presentation

data class UiState(
    val ram: MemoryState = MemoryState(),
    val storage: MemoryState = MemoryState()
)