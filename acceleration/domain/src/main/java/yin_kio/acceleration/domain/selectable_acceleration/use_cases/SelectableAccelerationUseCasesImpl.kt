package yin_kio.acceleration.domain.selectable_acceleration.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.*
import kotlin.coroutines.CoroutineContext

internal class SelectableAccelerationUseCasesImpl(
    private val outer: SelectableAccelerationOuter,
    private val appsForm: AppsForm,
    private val apps: Apps,
    private val ads: Ads,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) : SelectableAccelerationUseCases, AppsFormState {

    override fun close(navigator: SelectableAccelerationNavigator){
        navigator.close()
    }

    override fun switchSelectAllApps(){
        appsForm.switchSelectAll()
        outer.setSelectionStatus(appsForm.selectionStatus)
        outer.setSelectedApps(appsForm.selectedApps.toList())
    }

    override fun switchSelectApp(app: App, selectable: SelectableItem){
        appsForm.switchSelectApp(app)
        val isSelected = appsForm.isAppSelected(app)
        selectable.setSelected(isSelected)
        outer.setSelectionStatus(appsForm.selectionStatus)
        outer.setSelectedApps(appsForm.selectedApps.toList())
    }

    override fun updateList() = async{
        outer.setUpdateStatus(UpdateStatus.Loading)
        val appsList = apps.provide()
        appsForm.apps = appsList
        outer.setApps(appsList)
        outer.setSelectedApps(appsForm.selectedApps.toList())
        outer.setUpdateStatus(UpdateStatus.Complete)
    }

    override fun stopSelectedApps(navigator: SelectableAccelerationNavigator) = async{
        if (appsForm.selectionStatus != SelectionStatus.NoSelected){
            ads.preloadAd()
            val selectedApps = appsForm.selectedApps

            navigator.showStopProgress()
            apps.stop(selectedApps)
            delay(8000)
            navigator.showInter()
        }
    }

    override fun complete(navigator: SelectableAccelerationNavigator){
        navigator.complete()
    }

    private fun async(action: suspend CoroutineScope.() -> Unit){
        coroutineScope.launch(context = dispatcher,block = action)
    }

    override fun isAppSelected(app: App): Boolean {
        return appsForm.isAppSelected(app)
    }

    override fun updateAppItem(app: App, selectable: SelectableItem) {
        selectable.setSelected(appsForm.isAppSelected(app))
    }
}