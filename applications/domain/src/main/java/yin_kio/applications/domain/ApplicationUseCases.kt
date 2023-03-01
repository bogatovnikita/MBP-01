package yin_kio.applications.domain

class ApplicationUseCases(
    private val outer: Outer,
    private val appsInfo: AppsInfo,
    private val establishedAppsForm: EstablishedAppsForm,
    private val apps: Apps
) {

    fun update(){
        establishedAppsForm.apps = apps.provideEstablished()

        outer.outAppsInfo(appsInfo.provide())

        outer.outEstablishedApps(establishedAppsForm.apps)
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
        selectable.setSelected(establishedAppsForm.isAppSelected(app))
    }

}