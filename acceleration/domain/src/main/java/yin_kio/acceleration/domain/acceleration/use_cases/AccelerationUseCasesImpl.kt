package yin_kio.acceleration.domain.acceleration.use_cases

import yin_kio.acceleration.domain.acceleration.gateways.Permissions
import yin_kio.acceleration.domain.acceleration.gateways.RamInfo
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.gateways.Apps

internal class AccelerationUseCasesImpl(
    private val accelerationOuter: AccelerationOuter,
    private val permissions: Permissions,
    private val runner: Accelerator,
    private val ramInfo: RamInfo,
    private val apps: Apps
) : AccelerationUseCases {

    override fun close(){
        accelerationOuter.close()
    }

    override fun accelerate(){
        if (permissions.hasPermission){
            runner.accelerate()
        } else {
            accelerationOuter.showPermission()
        }

    }

    override fun uploadBackgroundProcess(){
        if (permissions.hasPermission){
            accelerationOuter.showSelectableAcceleration()
        } else {
            accelerationOuter.showPermission()
        }
    }

    override fun update(){
        accelerationOuter.showRamInfo(ramInfo.provide())
        accelerationOuter.showAppsState(AppsState.Progress)
        if (permissions.hasPermission){
            val appsList = apps.provide()
            accelerationOuter.showAppsState(AppsState.AppsList(appsList))
        } else {
            accelerationOuter.showAppsState(AppsState.Permission)
        }
    }

    override fun complete(){
        accelerationOuter.complete()
    }

    override fun givePermission() {
        accelerationOuter.givePermission()
    }
}