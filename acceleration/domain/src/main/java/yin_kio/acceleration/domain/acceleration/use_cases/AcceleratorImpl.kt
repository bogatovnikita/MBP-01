package yin_kio.acceleration.domain.acceleration.use_cases

import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.bg_uploading.entities.AppsForm
import yin_kio.acceleration.domain.gateways.Apps

internal class AcceleratorImpl(
    private val accelerationOuter: AccelerationOuter,
    private val appsForm: AppsForm,
    private val apps: Apps
) : Accelerator {

    override fun accelerate(){
        accelerationOuter.showAccelerateProgress()
        apps.stop(appsForm.selectedApps)
        accelerationOuter.showInter()
    }

}