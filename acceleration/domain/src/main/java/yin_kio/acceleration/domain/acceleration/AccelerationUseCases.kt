package yin_kio.acceleration.domain.acceleration

class AccelerationUseCases(
    private val accelerationOuter: AccelerationOuter,
    private val permissions: Permissions,
    private val runner: Accelerator,
    private val ramInfo: RamInfo
) {

    fun close(){
        accelerationOuter.close()
    }

    fun accelerate(){
        if (permissions.hasPermission){
            runner.accelerate()
        } else {
            accelerationOuter.showPermission()
        }

    }

    fun uploadBackgroundProcess(){
        if (permissions.hasPermission){
            accelerationOuter.showSelectableAcceleration()
        } else {
            accelerationOuter.showPermission()
        }
    }

    fun update(){
        accelerationOuter.showRamInfo(ramInfo.provide())
        if (permissions.hasPermission){
            accelerationOuter.showAppsList(listOf())
        } else {
            accelerationOuter.showPermissionOnList()
        }
    }

}