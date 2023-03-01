package yin_kio.applications.domain

class ApplicationUseCases(
    private val outer: Outer,
    private val appsInfo: AppsInfo
) {

    fun update(){
        outer.outAppsInfo(appsInfo.provide())
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

}