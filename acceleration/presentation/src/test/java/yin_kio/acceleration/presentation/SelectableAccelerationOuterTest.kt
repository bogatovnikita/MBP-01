package yin_kio.acceleration.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertTrue
import org.junit.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationOuterImpl
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationPresenter
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationViewModel

class SelectableAccelerationOuterTest  {

    private val navigator: SelectableAccelerationNavigator = spyk()
    private val presenter: SelectableAccelerationPresenter = mockk()
    private val outer: SelectableAccelerationOuterImpl = SelectableAccelerationOuterImpl(
        navigator = navigator,
        presenter = presenter
    )
    private val viewModel: SelectableAccelerationViewModel = spyk()

    init {
        outer.viewModel = viewModel
    }

    @Test
    fun testSetAppSelected(){
        assertSetAppSelected(true)
        assertSetAppSelected(false)
    }

    private fun assertSetAppSelected(isSelected: Boolean) {
        val packageName = "packageName"
        outer.setAppSelected(packageName, isSelected)

        coVerify { viewModel.setAppSelected(packageName, isSelected) }
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
        }
    }

}