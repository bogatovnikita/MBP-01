package yin_kio.applications.domain

interface EstablishedAppsForm {

    fun isAppSelected(app: App) : Boolean
    var content: List<App>
    fun sort()

}