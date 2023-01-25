package com.hedgehog.presentation.ui.second_screen

import com.hedgehog.presentation.models.AppInfo

data class SecondScreenTimeState(
    val isErrorLoading: Boolean = false,
    val appInfo: AppInfo = AppInfo()
    )