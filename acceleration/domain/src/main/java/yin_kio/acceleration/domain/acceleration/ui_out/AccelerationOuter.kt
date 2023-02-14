package yin_kio.acceleration.domain.acceleration.ui_out

interface AccelerationOuter {

    fun close()

    fun showAccelerateProgress()
    fun showInter()

    fun showPermission()
    fun showSelectableAcceleration()

    fun showRamInfo(ramInfo: RamInfoOut)

    fun showAppsState(appsState: AppsState)

    fun givePermission()


    fun complete()

}