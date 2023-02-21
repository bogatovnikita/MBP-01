package yin_kio.acceleration.presentation.selectable_acceleration

interface MutableSelectableAccelerationViewModel {
    fun setAppSelected(packageName: String, isSelected: Boolean)
    fun setButtonBgRes(resId: Int)
    fun setAllSelected(isAllSelected: Boolean)
    fun setProgressVisible(isVisible: Boolean)
    fun setListVisible(isVisible: Boolean)
    fun setApps(apps: List<String>)
}