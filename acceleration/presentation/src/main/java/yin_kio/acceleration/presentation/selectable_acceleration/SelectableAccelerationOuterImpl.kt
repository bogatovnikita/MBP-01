package yin_kio.acceleration.presentation.selectable_acceleration

import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus

class SelectableAccelerationOuterImpl(
    private val navigator: SelectableAccelerationNavigator
) : SelectableAccelerationOuter,
    SelectableAccelerationNavigator by navigator
{
    var viewModel: SelectableAccelerationViewModel? = null

    override fun setAppSelected(packageName: String, isSelected: Boolean) {
        viewModel?.setAppSelected(packageName, isSelected)
    }

    override fun setSelectionStatus(selectionStatus: SelectionStatus) {
        TODO("Not yet implemented")
    }

    override fun setUpdateStatus(updateStatus: UpdateStatus) {
        TODO("Not yet implemented")
    }

    override fun setApps(apps: List<String>) {
        TODO("Not yet implemented")
    }
}