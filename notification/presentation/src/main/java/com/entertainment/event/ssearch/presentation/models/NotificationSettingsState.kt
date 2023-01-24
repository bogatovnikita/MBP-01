package com.entertainment.event.ssearch.presentation.models

data class NotificationSettingsState(
    val modeNotDisturb: Boolean = false,
    val apps: List<AppUi> = emptyList(),
    val isAllAppsLimited: Boolean = false,
    val hasServicePermission: Boolean = false,
    val isAllNotificationCleared: Boolean = false,
    val tableTimeSate: TableTimeSate = TableTimeSate(),
    val event: NotificationStateEvent = NotificationStateEvent.Default,
)

sealed class NotificationStateEvent() {

    object Default: NotificationStateEvent()

    object OpenPermissionDialog: NotificationStateEvent()

    object ClearAllNotification: NotificationStateEvent()

    object Update: NotificationStateEvent()

    class SwitchGeneralDisturbMode(val isSwitch: Boolean) : NotificationStateEvent()

    class SwitchAppModeDisturb(val packageName: String, val isSwitch: Boolean) : NotificationStateEvent()

//    object OpenTimeTable: NotificationStateEvent()

    class LimitApps(val isSwitch: Boolean): NotificationStateEvent()

}