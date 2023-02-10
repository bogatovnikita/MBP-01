package yin_kio.memory.presentation

data class MemoryState(
    val progress: Float = 0f,
    val occupied: String = "",
    val available: String = "",
    val total: String = ""
)