package yin_kio.acceleration.domain.selectable_acceleration.ui_out

import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus

interface SelectableAccelerationOuter : SelectableAccelerationNavigator {

    fun updateApps()

    fun setAppSelected(packageName: String, isSelected: Boolean)

    fun setSelectionStatus(selectionStatus: SelectionStatus)

    fun setUpdateStatus(updateStatus: UpdateStatus)

    fun setApps(apps: List<String>)

}
