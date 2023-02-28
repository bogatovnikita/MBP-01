package yin_kio.acceleration.presentation.selectable_acceleration

import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus
import yin_kio.acceleration.presentation.selectable_acceleration.view_model.MutableSelectableAccelerationViewModel

class SelectableAccelerationOuterImpl(
    private val presenter: SelectableAccelerationPresenter
) : SelectableAccelerationOuter
{
    var viewModel: MutableSelectableAccelerationViewModel? = null

    override fun setSelectionStatus(selectionStatus: SelectionStatus) {
        viewModel?.setAllSelected(presenter.presentAllSelected(selectionStatus))
        viewModel?.setButtonBgRes(presenter.presentButtonBg(selectionStatus))
        viewModel?.updateApps()
    }

    override fun setUpdateStatus(updateStatus: UpdateStatus) {
        viewModel?.setProgressVisible(updateStatus == UpdateStatus.Loading)
        viewModel?.setListVisible(updateStatus == UpdateStatus.Complete)
    }

    override fun setApps(apps: List<App>) {
        viewModel?.setApps(apps)
    }

    override fun setSelectedApps(selectedApps: List<App>) {
        viewModel?.setSelectedApps(selectedApps)
    }
}