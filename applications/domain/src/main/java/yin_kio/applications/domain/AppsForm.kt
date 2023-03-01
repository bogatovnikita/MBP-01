package yin_kio.applications.domain

interface AppsForm {

    fun isAppSelected(app: App) : Boolean
    var systemApps: List<App>
    var establishedApps: List<App>

}