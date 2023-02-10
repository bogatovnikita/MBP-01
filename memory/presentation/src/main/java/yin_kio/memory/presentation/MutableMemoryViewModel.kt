package yin_kio.memory.presentation

interface MutableMemoryViewModel {
    suspend fun setRamState(ramState: MemoryState)
    suspend fun setStorageState(storageState: MemoryState)
    suspend fun notify()
}