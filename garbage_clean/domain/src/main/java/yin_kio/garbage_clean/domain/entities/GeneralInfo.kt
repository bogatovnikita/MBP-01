package yin_kio.garbage_clean.domain.entities

data class GeneralInfo(
    val occupied: Long,
    val available: Long,
    val total: Long
)