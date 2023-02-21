package yin_kio.acceleration.presentation.selectable_acceleration

data class ScreenState(
    val isAllSelected: Boolean = false,
    val buttonBgRes: Int = general.R.drawable.bg_main_button_disabled,
    val isProgressVisible: Boolean = true,
    val isListVisible: Boolean = false,
    val apps: List<String> = emptyList()
)