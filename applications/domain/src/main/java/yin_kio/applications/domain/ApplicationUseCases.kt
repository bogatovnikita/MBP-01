package yin_kio.applications.domain

class ApplicationUseCases(
    private val outer: Outer,
    private val appsInfo: AppsInfo,
    private val establishedAppsForm: EstablishedAppsForm,
    private val apps: Apps,
    private val systemAppsList: SystemAppsList
) {

    fun update(){
        establishedAppsForm.content = apps.provideEstablished()
        systemAppsList.content = apps.provideSystem()

        outer.outAppsInfo(appsInfo.provide())
        outer.outSystemApps(systemAppsList.content)
        outer.outEstablishedApps(establishedAppsForm.content)
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

    fun sortSystemApps(){
        systemAppsList.sort()
        outer.outSystemApps(systemAppsList.content)
    }

}