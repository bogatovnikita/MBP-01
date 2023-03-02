package yin_kio.applications.domain.core

interface EstablishedAppsForm {

    var content: List<App>
    var isVisible: Boolean
    val isAllSelected: Boolean
    val selectedApps: Collection<App>


    fun isAppSelected(app: App) : Boolean
    fun sort()
    fun switchIsAllSelected()

}