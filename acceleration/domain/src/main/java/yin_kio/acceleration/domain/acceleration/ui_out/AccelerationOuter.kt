package yin_kio.acceleration.domain.acceleration.ui_out

interface AccelerationOuter : AccelerationNavigator {

    fun showRamInfo(ramInfo: RamInfoOut)

    fun showAppsState(appsState: AppsState)

    fun givePermission()

}