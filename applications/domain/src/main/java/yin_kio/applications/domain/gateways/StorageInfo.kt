package yin_kio.applications.domain.gateways

interface StorageInfo {

    val freedSpace: Long

    fun saveStartVolume()
    fun saveEndVolume()

}