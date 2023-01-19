package com.entertainment.event.ssearch.presentation.ui.models

data class NotificationSettingsState(
    val modeNotDisturb: Boolean = false,
    val tableTimeSate: TableTimeSate = TableTimeSate(),
    val isAllNotificationCleared: Boolean = false,
    val apps: List<AppUi> = emptyList(),
)