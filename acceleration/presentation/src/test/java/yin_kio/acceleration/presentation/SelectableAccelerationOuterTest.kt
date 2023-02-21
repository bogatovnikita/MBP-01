package yin_kio.acceleration.presentation

import io.mockk.coVerify
import io.mockk.spyk
import junit.framework.TestCase.assertTrue
import org.junit.Test
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationOuterImpl
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationViewModel

class SelectableAccelerationOuterTest  {

    private val navigator: SelectableAccelerationNavigator = spyk()
    private val outer: SelectableAccelerationOuterImpl = SelectableAccelerationOuterImpl(
        navigator = navigator
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

}