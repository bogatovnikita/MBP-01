package yin_kio.acceleration.presentation.selectable_acceleration.view_model

import yin_kio.acceleration.domain.selectable_acceleration.entities.App

interface MutableSelectableAccelerationViewModel {
    fun setButtonBgRes(resId: Int)
    fun setAllSelected(isAllSelected: Boolean)
    fun setProgressVisible(isVisible: Boolean)
    fun setListVisible(isVisible: Boolean)
    fun setApps(apps: List<App>)
    fun updateApps()
    fun setSelectedApps(selectedApps: List<App>)
}