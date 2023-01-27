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
        viewModelScope.launch(Dispatchers.Default) { // TODO почему используется default?
            useCase.readAll().collect { notifications ->
                if (notifications.isNotEmpty())
                updateState {
                    it.copy(
                        notificationIsEmpty = false,
                        notifications= notifications.toNotificationUi()
                    )
                }
            }
        }
    }

    fun obtainEvent(event: MissedNotificationEvent) {
        when (event) {
            is MissedNotificationEvent.OpenAppByPackageName -> openAppAndDeleteNotification(event.notificationUi)
            is MissedNotificationEvent.CleanAll -> cleanAll()
            else -> {}
        }
    }

    private fun cleanAll() {
        viewModelScope.launch { // TODO здесь не используется диспетчер вообще. Надо IO
            useCase.deleteAll()
            updateState {
                it.copy(
                    notifications = emptyList(),
                    notificationIsEmpty = true,
                )
            }
        }
    }

    private fun openAppAndDeleteNotification(notification: NotificationUi) {
        cleanNotification(notification)
        useCase.startAppByPackageName(notification.packageName)
    }

    private fun cleanNotification(notification: NotificationUi) { //TODO обычно, если хотят удалить все уведомления пишут clear. Лучше назвать deleteNotification
        viewModelScope.launch {// TODO здесь не используется диспетчер вообще. Надо IO
            useCase.delete(notification.toNotification())
        }
    }
}