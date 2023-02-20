package yin_kio.acceleration.presentation.acceleration.screen

import yin_kio.acceleration.domain.acceleration.ui_out.AppsState

data class ScreenState(
    val ramInfo: RamInfo = RamInfo(),
    val appsState: AppsState = AppsState.Progress
)