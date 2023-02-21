package yin_kio.acceleration.domain.selectable_acceleration.use_cases

interface SelectableAccelerationUseCases {
    fun close()

    fun switchSelectAllApps()
    fun switchSelectApp(packageName: String)
    fun updateList()
    fun updateListItem(packageName: String)
    fun stopSelectedApps()

    fun complete()
}