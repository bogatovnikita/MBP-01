package com.entertainment.event.ssearch.presentation.ui.missed_notifications

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.MissedNotificationUseCase
import com.entertainment.event.ssearch.presentation.mappers.toNotification
import com.entertainment.event.ssearch.presentation.mappers.toNotificationUi
import com.entertainment.event.ssearch.presentation.models.MissedNotificationEvent
import com.entertainment.event.ssearch.presentation.models.MissedNotificationState
import com.entertainment.event.ssearch.presentation.models.NotificationUi
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissedNotificationViewModel @Inject constructor(
    private val useCase: MissedNotificationUseCase
): BaseViewModel<MissedNotificationState>(MissedNotificationState()) {

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.readAll().collect { notifications ->
                updateState {
                    it.copy(
                        notificationIsEmpty = notifications.isEmpty(),
                        notifications= notifications.toNotificationUi()
                    )
                }
            }
        }
    }

    fun obtainEvent(event: MissedNotificationEvent) {
        when (event) {
            is MissedNotificationEvent.OpenAppByPackageName -> openAppAndDeleteNotification(event.notificationUi)
            is MissedNotificationEvent.DeleteAll -> deleteAll(event.isCanDelete)
            is MissedNotificationEvent.DeleteNotification -> deleteNotification(event.notificationUi)
            else -> {}
        }
    }

    private fun deleteAll(isCanDelete: Boolean) {
        if (!isCanDelete) return
        viewModelScope.launch(Dispatchers.IO) {
            useCase.deleteAll()
        }
    }

    private fun openAppAndDeleteNotification(notification: NotificationUi) {
        deleteNotification(notification)
        useCase.startAppByPackageName(notification.packageName)
    }

    private fun deleteNotification(notification: NotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.delete(notification.toNotification())
        }
    }
}