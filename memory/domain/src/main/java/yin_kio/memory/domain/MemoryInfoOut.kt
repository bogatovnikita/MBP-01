package yin_kio.memory.domain

data class MemoryInfoOut(
    val occupied: Long = 0,
    val available: Long = 0,
    val total: Long = 0
)