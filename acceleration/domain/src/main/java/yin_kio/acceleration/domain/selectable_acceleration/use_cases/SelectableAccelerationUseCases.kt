package yin_kio.acceleration.domain.selectable_acceleration.use_cases

import yin_kio.acceleration.domain.selectable_acceleration.ui_out.AppsFormState

interface SelectableAccelerationUseCases : AppsFormState {


    fun close()

    fun switchSelectAllApps()
    fun switchSelectApp(packageName: String)
    fun updateList()
    fun updateListItem(packageName: String)
    fun stopSelectedApps()

    fun complete()
}