package yin_kio.acceleration.domain.acceleration

import yin_kio.acceleration.domain.bg_uploading.AppsForm
import yin_kio.acceleration.domain.gateways.Apps

class AcceleratorImpl(
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