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
        outer.setButtonEnabled(appsForm.hasSelected)
        outer.updateApps()
    }

    fun switchSelectApp(packageName: String){
        appsForm.switchSelectApp(packageName)
        val isSelected = appsForm.isAppSelected(packageName)
        outer.setAppSelected(packageName, isSelected)
        outer.setButtonEnabled(appsForm.hasSelected)
        outer.setAllSelected(appsForm.isAllSelected)
    }


}