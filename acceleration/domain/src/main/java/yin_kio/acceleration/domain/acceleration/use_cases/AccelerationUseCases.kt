package yin_kio.acceleration.domain.acceleration.use_cases

interface AccelerationUseCases {
    fun close()

    fun accelerate()
    fun uploadBackgroundProcess()
    fun update()

    fun complete()
}