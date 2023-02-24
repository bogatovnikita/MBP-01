package yin_kio.acceleration.domain.selectable_acceleration.use_cases

import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.AppsFormState
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableItem

interface SelectableAccelerationUseCases : AppsFormState {


    fun close()

    fun switchSelectAllApps()
    fun switchSelectApp(app: App, selectable: SelectableItem)
    fun updateAppItem(app: App, selectable: SelectableItem)
    fun updateList()
    fun stopSelectedApps()

    fun complete()
}