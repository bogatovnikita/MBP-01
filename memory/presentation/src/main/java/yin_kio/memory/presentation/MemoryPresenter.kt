package yin_kio.memory.presentation

import yin_kio.memory.domain.MemoryInfoOut

interface MemoryPresenter {

    suspend fun presentMemoryState(memoryInfoOut: MemoryInfoOut) : MemoryState

}