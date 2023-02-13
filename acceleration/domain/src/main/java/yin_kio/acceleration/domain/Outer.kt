package yin_kio.acceleration.domain

interface Outer {

    fun close()

    fun showAccelerateProgress()
    fun showInter()
    fun complete()

    fun showPermission()
    fun showSelectableAcceleration()

    fun showRamInfo(ramInfo: RamInfoOut)

    fun showAppsList(apps: List<String>)
    fun showPermissionOnList()

}