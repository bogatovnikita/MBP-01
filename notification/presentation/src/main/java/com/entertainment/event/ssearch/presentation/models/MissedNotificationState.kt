package com.entertainment.event.ssearch.presentation.models

data class MissedNotificationState(
    val notificationIsEmpty: Boolean = true,
    val notifications: List<NotificationUi> = emptyList(),
    val event: MissedNotificationEvent = MissedNotificationEvent.Default,
)

sealed class MissedNotificationEvent {

    object CleanAll : MissedNotificationEvent()

    class CleanNotification(val notificationUi: NotificationUi) : MissedNotificationEvent()

    object Default : MissedNotificationEvent()

    class OpenAppByPackageName(val notificationUi: NotificationUi) : MissedNotificationEvent()

}