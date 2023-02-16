package yin_kio.acceleration.domain.acceleration.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.acceleration.gateways.Permissions
import yin_kio.acceleration.domain.acceleration.gateways.RamInfo
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.domain.gateways.Apps
import kotlin.coroutines.CoroutineContext

internal class AccelerationUseCasesImpl(
    private val accelerationOuter: AccelerationOuter,
    private val permissions: Permissions,
    private val runner: Accelerator,
    private val ramInfo: RamInfo,
    private val apps: Apps,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) : AccelerationUseCases {

    override fun close() = async {
        accelerationOuter.close()
    }

    override fun accelerate() = async {
        if (permissions.hasPermission){
            runner.accelerate()
        } else {
            accelerationOuter.showPermission()
        }

    }

    override fun uploadBackgroundProcess() = async {
        if (permissions.hasPermission){
            accelerationOuter.showSelectableAcceleration()
        } else {
            accelerationOuter.showPermission()
        }
    }

    override fun update() = async {
        accelerationOuter.showRamInfo(ramInfo.provide())
        accelerationOuter.showAppsState(AppsState.Progress)
        if (permissions.hasPermission){
            val appsList = apps.provide()
            accelerationOuter.showAppsState(AppsState.AppsList(appsList))
        } else {
            accelerationOuter.showAppsState(AppsState.Permission)
        }
    }

    override fun complete() = async {
        accelerationOuter.complete()
    }

    override fun givePermission() = async {
        accelerationOuter.givePermission()
    }

    private fun async(action: suspend CoroutineScope.() -> Unit){
        coroutineScope.launch(context = dispatcher,block = action)
    }
}