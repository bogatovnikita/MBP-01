package yin_kio.acceleration.domain.selectable_acceleration.entities

internal interface AppsForm {
    val selectionStatus: SelectionStatus
    var apps: List<App>
    val selectedApps: Collection<App>


    fun switchSelectAll()

    fun switchSelectApp(app: App)
    fun isAppSelected(app: App) : Boolean

}