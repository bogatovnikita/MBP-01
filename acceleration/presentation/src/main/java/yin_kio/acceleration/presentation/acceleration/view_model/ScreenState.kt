package yin_kio.acceleration.presentation.acceleration.view_model

import yin_kio.acceleration.domain.acceleration.ui_out.AppsState

data class ScreenState(
    val ramInfo: RamInfo = RamInfo(),
    val appsState: AppsState = AppsState.Progress
)