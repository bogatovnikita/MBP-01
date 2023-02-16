package yin_kio.acceleration.domain.bg_uploading

import yin_kio.acceleration.domain.gateways.Apps

class BackgroundUploadingUseCases(
    private val outer: BackgroundUploadingOuter,
    private val appsForm: AppsForm,
    private val apps: Apps
) {

    fun close(){
        outer.close()
    }

    fun switchSelectAllApps(){
        appsForm.switchSelectAll()
        outer.setSelectionStatus(appsForm.selectionStatus)
        outer.updateApps()
    }

    fun switchSelectApp(packageName: String){
        appsForm.switchSelectApp(packageName)
        val isSelected = appsForm.isAppSelected(packageName)
        outer.setAppSelected(packageName, isSelected)
        outer.setSelectionStatus(appsForm.selectionStatus)
    }

    fun update(){
        outer.setUpdateStatus(UpdateStatus.Loading)
        val appsList = apps.provide()
        appsForm.apps = appsList
        outer.setApps(appsList)
        outer.setUpdateStatus(UpdateStatus.Complete)
    }


}