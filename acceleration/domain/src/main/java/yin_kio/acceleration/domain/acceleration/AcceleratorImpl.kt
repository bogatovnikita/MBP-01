package yin_kio.acceleration.domain.acceleration

class AcceleratorImpl(
    private val accelerationOuter: AccelerationOuter
) : Accelerator {

    override fun accelerate(){
        accelerationOuter.showAccelerateProgress()
        accelerationOuter.showInter()
        accelerationOuter.complete()
    }

}