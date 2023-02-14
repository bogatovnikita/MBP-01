package yin_kio.acceleration.domain.acceleration

interface AccelerationUseCases {
    fun close()

    fun accelerate()
    fun uploadBackgroundProcess()
    fun update()

    fun complete()
}