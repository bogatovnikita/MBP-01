package yin_kio.acceleration.domain.acceleration.use_cases

interface AccelerationUseCases {
    fun close()

    fun accelerate()
    fun uploadBackgroundProcess() // Может показаться, что этот метод дублируется, в пакете SelectableAcceleration, но это не так.
    fun update()
    fun givePermission()

    fun complete()
}