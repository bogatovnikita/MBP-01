package yin_kio.acceleration.domain.selectable_acceleration.entities

internal interface AppsForm {
    val selectionStatus: SelectionStatus
    var apps: List<String>
    val selectedApps: Collection<String>


    fun switchSelectAll()

    fun switchSelectApp(packageName: String)
    fun isAppSelected(packageName: String) : Boolean

}