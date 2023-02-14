package yin_kio.acceleration.domain.acceleration

import yin_kio.acceleration.domain.gateways.Apps

class AccelerationUseCases(
    private val accelerationOuter: AccelerationOuter,
    private val permissions: Permissions,
    private val runner: Accelerator,
    private val ramInfo: RamInfo,
    private val apps: Apps
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
            val appsList = apps.provide()
            accelerationOuter.showAppsList(appsList)
        } else {
            accelerationOuter.showPermissionOnList()
        }
    }

}