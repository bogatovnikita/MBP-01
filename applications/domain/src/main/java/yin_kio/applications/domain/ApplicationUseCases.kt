package yin_kio.applications.domain

class ApplicationUseCases(
    private val outer: Outer,
    private val appsInfo: AppsInfo,
    private val appsForm: AppsForm,
    private val apps: Apps
) {

    fun update(){
        outer.outAppsInfo(appsInfo.provide())
        outer.outSystemApps(apps.provideSystem())
        outer.outEstablishedApps(apps.provideEstablished())
    }

    fun close(navigator: Navigator){
        navigator.close()
    }

    fun askDelete(navigator: Navigator){
        navigator.showAskDeleteDialog()
    }

    fun delete(navigator: Navigator){
        navigator.showDeleteProgressDialog()
        navigator.showInter()
    }

    fun complete(navigator: Navigator){
        navigator.complete()
    }

    fun selectApp(app: App, selectable: Selectable){
        selectable.setSelected(appsForm.isAppSelected(app))
    }

}