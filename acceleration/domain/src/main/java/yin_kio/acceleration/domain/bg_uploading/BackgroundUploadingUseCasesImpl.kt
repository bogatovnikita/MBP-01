package yin_kio.acceleration.domain.bg_uploading

import yin_kio.acceleration.domain.gateways.Apps

internal class BackgroundUploadingUseCasesImpl(
    private val outer: BackgroundUploadingOuter,
    private val appsForm: AppsForm,
    private val apps: Apps
) : BackgroundUploadingUseCases {

    override fun close(){
        outer.close()
    }

    override fun switchSelectAllApps(){
        appsForm.switchSelectAll()
        outer.setSelectionStatus(appsForm.selectionStatus)
        outer.updateApps()
    }

    override fun switchSelectApp(packageName: String){
        appsForm.switchSelectApp(packageName)
        val isSelected = appsForm.isAppSelected(packageName)
        outer.setAppSelected(packageName, isSelected)
        outer.setSelectionStatus(appsForm.selectionStatus)
    }

    override fun update(){
        outer.setUpdateStatus(UpdateStatus.Loading)
        val appsList = apps.provide()
        appsForm.apps = appsList
        outer.setApps(appsList)
        outer.setUpdateStatus(UpdateStatus.Complete)
    }

    override fun stopSelectedApps(){
        val selectedApps = appsForm.selectedApps

        outer.showStopProgress()
        apps.stop(selectedApps)
        outer.showInter()
    }

    override fun complete(){
        outer.complete()
    }


}