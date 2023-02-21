package yin_kio.acceleration.domain.selectable_acceleration.use_cases

interface SelectableAccelerationUseCases {
    fun close()

    fun switchSelectAllApps()
    fun switchSelectApp(packageName: String)
    fun update()
    fun stopSelectedApps()

    fun complete()
}