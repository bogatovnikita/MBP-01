package yin_kio.acceleration.presentation.selectable_acceleration

import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus

class SelectableAccelerationOuterImpl(
    private val navigator: SelectableAccelerationNavigator,
    private val presenter: SelectableAccelerationPresenter
) : SelectableAccelerationOuter,
    SelectableAccelerationNavigator by navigator
{
    var viewModel: SelectableAccelerationViewModel? = null

    override fun setAppSelected(packageName: String, isSelected: Boolean) {
        viewModel?.setAppSelected(packageName, isSelected)
    }

    override fun setSelectionStatus(selectionStatus: SelectionStatus) {
        viewModel?.setAllSelected(presenter.presentAllSelected(selectionStatus))
        viewModel?.setButtonBgRes(presenter.presentButtonBg(selectionStatus))
    }

    override fun setUpdateStatus(updateStatus: UpdateStatus) {
        viewModel?.setProgressVisible(updateStatus == UpdateStatus.Loading)
        viewModel?.setListVisible(updateStatus == UpdateStatus.Complete)
    }

    override fun setApps(apps: List<String>) {
        viewModel?.setApps(apps)
    }
}