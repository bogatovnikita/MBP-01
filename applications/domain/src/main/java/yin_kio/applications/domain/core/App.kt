package yin_kio.applications.domain.core

data class App(
    val packageName: String = "",
    val name: String = "",
    val icon: Any? = null,
    val size: Long = 0
)