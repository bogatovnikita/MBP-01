package yin_kio.acceleration.domain

class AccelerationUseCases(
    private val outer: Outer,
    private val permissions: Permissions,
    private val runner: AccelerateRunner,
    private val ramInfo: RamInfo
) {

    fun close(){
        outer.close()
    }

    fun accelerate(){
        if (permissions.hasPermission){
            runner.run()
        } else {
            outer.showPermission()
        }

    }

    fun uploadBackgroundProcess(){
        if (permissions.hasPermission){
            outer.showSelectableAcceleration()
        } else {
            outer.showPermission()
        }
    }

    fun update(){
        outer.showRamInfo(ramInfo.provide())
    }

}