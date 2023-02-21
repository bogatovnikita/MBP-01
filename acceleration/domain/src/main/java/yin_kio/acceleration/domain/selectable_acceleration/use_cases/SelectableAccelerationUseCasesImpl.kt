package yin_kio.acceleration.domain.selectable_acceleration.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import kotlin.coroutines.CoroutineContext

internal class SelectableAccelerationUseCasesImpl(
    private val outer: SelectableAccelerationOuter,
    private val appsForm: AppsForm,
    private val apps: Apps,
    private val ads: Ads,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) : SelectableAccelerationUseCases {

    override fun close(){
        outer.close()
    }

    override fun switchSelectAllApps(){
        appsForm.switchSelectAll()
        outer.setSelectionStatus(appsForm.selectionStatus)
    }

    override fun switchSelectApp(packageName: String){
        appsForm.switchSelectApp(packageName)
        val isSelected = appsForm.isAppSelected(packageName)
        outer.setAppSelected(packageName, isSelected)
        outer.setSelectionStatus(appsForm.selectionStatus)
    }

    override fun updateList() = async{
        outer.setUpdateStatus(UpdateStatus.Loading)
        val appsList = apps.provide()
        appsForm.apps = appsList
        outer.setApps(appsList)
        outer.setUpdateStatus(UpdateStatus.Complete)
    }

    override fun stopSelectedApps() = async{
        ads.preloadAd()
        val selectedApps = appsForm.selectedApps

        outer.showStopProgress()
        apps.stop(selectedApps)
        delay(8000)
        outer.showInter()
    }

    override fun complete(){
        outer.complete()
    }

    override fun updateListItem(packageName: String) = async {
        outer.setAppSelected(packageName, appsForm.isAppSelected(packageName))
    }

    private fun async(action: suspend CoroutineScope.() -> Unit){
        coroutineScope.launch(context = dispatcher,block = action)
    }


}