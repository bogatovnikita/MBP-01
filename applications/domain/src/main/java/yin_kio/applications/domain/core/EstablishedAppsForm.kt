package yin_kio.applications.domain.core

interface EstablishedAppsForm {

    fun isAppSelected(app: App) : Boolean
    var content: List<App>
    fun sort()

}