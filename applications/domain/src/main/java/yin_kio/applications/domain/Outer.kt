package yin_kio.applications.domain

interface Outer {
    fun outAppsInfo(appsInfoOut: AppsInfoOut)
    fun outSystemApps(apps: List<App>)
    fun outEstablishedApps(apps: List<App>)
}