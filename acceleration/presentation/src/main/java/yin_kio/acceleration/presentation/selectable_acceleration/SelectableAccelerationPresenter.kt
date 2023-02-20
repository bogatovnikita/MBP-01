package yin_kio.acceleration.presentation.selectable_acceleration

import yin_kio.acceleration.domain.selectable_acceleration.entities.SelectionStatus

class SelectableAccelerationPresenter {

    fun presentButtonBg(selectionStatus: SelectionStatus) : Int{
        return when(selectionStatus){
            SelectionStatus.AllSelected -> general.R.drawable.bg_main_button_enabled
            else -> general.R.drawable.bg_main_button_disabled
        }
    }

    fun presentAllSelected(selectionStatus: SelectionStatus) : Boolean{
        return selectionStatus == SelectionStatus.AllSelected
    }

}