package com.hedgehog.presentation.ui.first_screen

import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.AppScreenTimeListItems

data class FirstScreenTimeState(
    val listDataScreenTime: List<AppScreenTime> = emptyList(),
    val isLoading: Boolean = false,
    val isErrorLoading: Boolean = false,
    val choiceDay: Boolean = true,
    val choiceWeek: Boolean = false,
    val selectionMode: Boolean = false,
    val totalCount: Int = 0,
    val totalCheckedCount: Int = 0,
    val appScreenTimeListItems: List<AppScreenTimeListItems> = emptyList(),
    val reverseListAppScreenTime: Boolean = false
)