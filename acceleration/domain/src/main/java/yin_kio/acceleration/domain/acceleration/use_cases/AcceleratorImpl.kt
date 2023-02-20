package yin_kio.acceleration.domain.acceleration.use_cases

import kotlinx.coroutines.delay
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsForm
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps

internal class AcceleratorImpl(
    private val accelerationOuter: AccelerationOuter,
    private val appsForm: AppsForm,
    private val apps: Apps,
    private val ads: Ads
) : Accelerator {

    override suspend fun accelerate(){
        ads.preloadAd()
        accelerationOuter.showAccelerateProgress()
        apps.stop(appsForm.selectedApps)
        delay(8000)
        accelerationOuter.showInter()
    }

}