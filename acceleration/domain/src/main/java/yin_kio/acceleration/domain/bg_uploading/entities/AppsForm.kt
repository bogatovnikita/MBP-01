package yin_kio.acceleration.domain.bg_uploading.entities

internal interface AppsForm {

    val hasSelected: Boolean
    val isAllSelected: Boolean
    val selectionStatus: SelectionStatus
    var apps: List<String>
    val selectedApps: Collection<String>


    fun switchSelectAll()

    fun switchSelectApp(packageName: String)
    fun isAppSelected(packageName: String) : Boolean

}