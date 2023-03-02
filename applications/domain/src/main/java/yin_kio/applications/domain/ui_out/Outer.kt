package yin_kio.applications.domain.ui_out

import yin_kio.applications.domain.core.App

interface Outer {
    fun outAppsInfo(appsInfoOut: AppsInfoOut)
    fun outSystemApps(apps: List<App>)
    fun outEstablishedApps(apps: List<App>)
    fun expandEstablishedApps()
    fun expandSystemApps()
    fun collapseSystemApps()
    fun collapseEstablishedApps()
    fun setIsAllSelected(isAllSelected: Boolean)
}