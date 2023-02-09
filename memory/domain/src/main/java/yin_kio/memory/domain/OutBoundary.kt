package yin_kio.memory.domain

interface OutBoundary {

    fun outMemoryInfos(ramInfo: MemoryInfoOut, storageInfo: MemoryInfoOut)

}