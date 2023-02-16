package com.entertainment.event.ssearch.presentation.models

data class NotificationSettingsState(
    val timeEnd: Int = 0,
    val timeStart: Int = 0,
    val modeDND: Boolean = false,
    val apps: List<AppUi> = emptyList(),
    val isAutoModeEnable: Boolean = false,
    val isAllAppsLimited: Boolean = false,
    val isNeedShowTimetableInfo: Boolean = false,
    val hasServicePermission: Boolean = false,
    val selectedDays: List<Int> = emptyList(),
    val isAllNotificationCleared: Boolean = false,
    val event: NotificationStateEvent = NotificationStateEvent.Default,
)

sealed class NotificationStateEvent() {

    object Default: NotificationStateEvent()

    object OpenServicePermissionDialog: NotificationStateEvent()

    object OpenNotificationPermissionDialog: NotificationStateEvent()

    object ClearAllNotification: NotificationStateEvent()

    object OpenDialogClearing: NotificationStateEvent()

    object CloseDialogClearing: NotificationStateEvent()

    object OpenDialogCompleteClean: NotificationStateEvent()

    object Update: NotificationStateEvent()

    class SwitchGeneralDisturbMode(val isSwitch: Boolean) : NotificationStateEvent()

    class SwitchAppModeDisturb(val packageName: String, val isSwitch: Boolean) : NotificationStateEvent()

    object OpenTimeTable: NotificationStateEvent()

    object OpenMissedNotification: NotificationStateEvent()

    class LimitApps(val isSwitch: Boolean): NotificationStateEvent()

}