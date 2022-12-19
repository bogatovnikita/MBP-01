package com.hedgehog.presentation.ui.first_screen

import android.app.usage.UsageStats

data class FirstScreenTimeState(
    val listDataScreenTime: List<UsageStats> = emptyList(),
    val isLoading: Boolean = false,
    val isErrorLoading: Boolean = false
)