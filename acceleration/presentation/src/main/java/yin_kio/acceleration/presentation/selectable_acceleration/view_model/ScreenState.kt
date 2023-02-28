package yin_kio.acceleration.presentation.selectable_acceleration.view_model

import yin_kio.acceleration.domain.selectable_acceleration.entities.App

data class ScreenState(
    val isAllSelected: Boolean = false,
    val buttonBgRes: Int = general.R.drawable.bg_main_button_disabled,
    val isProgressVisible: Boolean = true,
    val isListVisible: Boolean = false,
    val apps: List<App> = emptyList(),
    val selectedApps: List<App> = emptyList()
)