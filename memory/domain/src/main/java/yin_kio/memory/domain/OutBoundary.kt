package yin_kio.memory.domain

interface OutBoundary {

    suspend fun outMemoryInfos(ramInfo: MemoryInfoOut, storageInfo: MemoryInfoOut)

}