package yin_kio.memory.domain

interface StorageInfo {

    suspend fun provide() : MemoryInfoOut

}