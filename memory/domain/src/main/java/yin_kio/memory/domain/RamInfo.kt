package yin_kio.memory.domain

interface RamInfo {

    suspend fun provide() : MemoryInfoOut

}