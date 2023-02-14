package yin_kio.acceleration.domain.bg_uploading

class BackgroundUploadingUseCases(
    private val outer: BackgroundUploadingOuter,
    private val appsForm: AppsForm
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


}