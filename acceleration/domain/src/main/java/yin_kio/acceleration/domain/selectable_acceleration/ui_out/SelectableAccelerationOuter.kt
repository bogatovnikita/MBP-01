package yin_kio.acceleration.domain.selectable_acceleration.ui_out

import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus

interface SelectableAccelerationOuter {

    fun setSelectionStatus(selectionStatus: SelectionStatus)

    fun setUpdateStatus(updateStatus: UpdateStatus)

    fun setApps(apps: List<App>)

    fun setSelectedApps(selectedApps: List<App>)

}
