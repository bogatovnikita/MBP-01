package com.entertainment.event.ssearch.presentation.ui.models

import com.entertainment.event.ssearch.domain.models.AppWithNotificationsDomain

data class NotificationSettingsState(
    val modeNotDisturb: Boolean = false,
    val tableTimeSate: TableTimeSate = TableTimeSate(),
    val isAllNotificationCleared: Boolean = false,
    val apps: List<AppWithNotificationsDomain> = emptyList(),
)