package yin_kio.acceleration.presentation

import junit.framework.TestCase.*
import org.junit.Test
import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationPresenter

class SelectableAccelerationPresenterTest {


    private val presenter = SelectableAccelerationPresenter()

    @Test
    fun testPresentButtonBg(){
        assertBg(general.R.drawable.bg_main_button_enabled, SelectionStatus.AllSelected)
        assertBg(general.R.drawable.bg_main_button_disabled, SelectionStatus.NoSelected)
        assertBg(general.R.drawable.bg_main_button_disabled, SelectionStatus.HasSelected)
    }

    private fun assertBg(expected: Int, input: SelectionStatus){
        assertEquals(expected, presenter.presentButtonBg(input))
    }

    @Test
    fun testPresentAllSelected(){
        assertTrue(presenter.presentAllSelected(SelectionStatus.AllSelected))
        assertFalse(presenter.presentAllSelected(SelectionStatus.NoSelected))
        assertFalse(presenter.presentAllSelected(SelectionStatus.HasSelected))
    }

}