package yin_kio.acceleration.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.UpdateStatus
import yin_kio.acceleration.presentation.selectable_acceleration.MutableSelectableAccelerationViewModel
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationOuterImpl
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationPresenter

class SelectableAccelerationOuterTest  {

    private val navigator: SelectableAccelerationNavigator = spyk()
    private val presenter: SelectableAccelerationPresenter = mockk()
    private val outer: SelectableAccelerationOuterImpl = SelectableAccelerationOuterImpl(
        navigator = navigator,
        presenter = presenter
    )
    private val viewModel: MutableSelectableAccelerationViewModel = spyk()

    init {
        outer.viewModel = viewModel
    }


    @Test
    fun testSetSelectionStatus(){
        assertSelectionStatusOuts(0, false)
        assertSelectionStatusOuts(1, true)
    }

    private fun assertSelectionStatusOuts(
        someRes: Int,
        isAllSelected: Boolean
    ) {
        val anyStatus = SelectionStatus.NoSelected
        coEvery { presenter.presentButtonBg(anyStatus) } returns someRes
        coEvery { presenter.presentAllSelected(anyStatus) } returns isAllSelected


        outer.setSelectionStatus(anyStatus)

        coVerify {
            viewModel.setButtonBgRes(someRes)
            viewModel.setAllSelected(isAllSelected)
            viewModel.updateApps()
        }
    }

    @Test
    fun testSetUpdateStatus(){
        assertUpdateStatusOuts(UpdateStatus.Loading, isProgressVisible = true, isListVisible = false)
        assertUpdateStatusOuts(UpdateStatus.Complete, isProgressVisible = false, isListVisible = true)
    }

    private fun assertUpdateStatusOuts(
        updateStatus: UpdateStatus,
        isProgressVisible: Boolean,
        isListVisible: Boolean
    ) {
        outer.setUpdateStatus(updateStatus)

        coVerify {
            viewModel.setProgressVisible(isProgressVisible)
            viewModel.setListVisible(isListVisible)
        }
    }

    @Test
    fun testSetApps(){
        val apps = emptyList<App>()

        outer.setApps(apps)

        coVerify {
            viewModel.setApps(apps)
        }
    }

}