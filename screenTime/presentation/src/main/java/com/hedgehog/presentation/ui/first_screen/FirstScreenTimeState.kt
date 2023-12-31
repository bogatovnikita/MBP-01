package com.hedgehog.presentation.ui.first_screen

import com.hedgehog.presentation.models.AppScreenTime

data class FirstScreenTimeState(
    val listDataScreenTime: List<AppScreenTime> = emptyList(),
    val isLoading: Boolean = false,
    val isErrorLoading: Boolean = false,
    val choiceDay: Boolean = true,
    val choiceWeek: Boolean = false,
    val selectionMode: Boolean = false,
    var totalCheckedCount: Int = 0,
    var systemCheckedCount: Int = 0,
    val reverseListAppScreenTime: Boolean = false
)