package yin_kio.applications.domain

import yin_kio.applications.domain.core.App
import yin_kio.applications.domain.core.EstablishedAppsForm
import yin_kio.applications.domain.core.SystemAppsList
import yin_kio.applications.domain.gateways.Apps
import yin_kio.applications.domain.gateways.AppsInfo
import yin_kio.applications.domain.gateways.StorageInfo
import yin_kio.applications.domain.ui_out.DeleteRequester
import yin_kio.applications.domain.ui_out.Navigator
import yin_kio.applications.domain.ui_out.Outer
import yin_kio.applications.domain.ui_out.Selectable

class ApplicationUseCases(
    private val outer: Outer,
    private val appsInfo: AppsInfo,
    private val establishedAppsForm: EstablishedAppsForm,
    private val apps: Apps,
    private val systemAppsList: SystemAppsList,
    private val storageInfo: StorageInfo
) {

    fun updateAppsInfo(){
        outer.outAppsInfo(appsInfo.provide())
    }

    fun updateSystemApps(){
        systemAppsList.content = apps.provideSystem()
        systemAppsList.isVisible = true
        outer.outSystemApps(systemAppsList.content)
        outer.expandSystemApps()
    }

    fun updateEstablishedApps(){
        establishedAppsForm.content = apps.provideEstablished()
        establishedAppsForm.isVisible = true
        outer.outEstablishedApps(establishedAppsForm.content)
        outer.expandEstablishedApps()
    }

    fun close(navigator: Navigator){
        navigator.close()
    }

    fun askDelete(navigator: Navigator){
        navigator.showAskDeleteDialog()
    }

    fun delete(navigator: Navigator, deleteRequester: DeleteRequester){
        navigator.showDeleteProgressDialog()

        storageInfo.saveStartVolume()
        deleteRequester.delete(establishedAppsForm.selectedApps)
        storageInfo.saveEndVolume()

        navigator.showInter()
    }

    fun complete(navigator: Navigator){
        navigator.complete(storageInfo.freedSpace)
    }

    fun selectApp(app: App, selectable: Selectable){
        selectable.setSelected(establishedAppsForm.isAppSelected(app))
    }

    fun sortSystemApps(){
        systemAppsList.sort()
        outer.outSystemApps(systemAppsList.content)
    }

    fun sortEstablishedApps(){
        establishedAppsForm.sort()
        outer.outEstablishedApps(establishedAppsForm.content)
    }

    fun collapseSystemApps(){
        systemAppsList.isVisible = false
        outer.collapseSystemApps()
    }

    fun collapseEstablishedApps(){
        establishedAppsForm.isVisible = false
        outer.collapseEstablishedApps()
    }

    fun switchIsAllSelected(){
        establishedAppsForm.switchIsAllSelected()
        outer.setIsAllSelected(establishedAppsForm.isAllSelected)
    }

    fun cancelDelete(navigator: Navigator){
        navigator.close()
    }



}