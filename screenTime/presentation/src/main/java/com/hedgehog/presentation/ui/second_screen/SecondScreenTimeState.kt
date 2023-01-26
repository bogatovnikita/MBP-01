package com.hedgehog.presentation.ui.second_screen

import com.hedgehog.presentation.models.AppInfo

data class SecondScreenTimeState(
    val isLoading: Boolean = false,
    val isErrorLoading: Boolean = false,
    val appInfo: AppInfo = AppInfo(),
    val choiceDay: Boolean = true,
    val choiceWeek: Boolean = false,
)