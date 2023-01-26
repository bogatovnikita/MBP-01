package com.entertainment.event.ssearch.presentation.ui.notification_manager

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.NotificationSettingsUseCases
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import com.entertainment.event.ssearch.presentation.mappers.mapToAppUiList
import com.entertainment.event.ssearch.presentation.models.NotificationSettingsState
import com.entertainment.event.ssearch.presentation.models.NotificationStateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val useCases: NotificationSettingsUseCases,
) : BaseViewModel<NotificationSettingsState>(NotificationSettingsState()) {

    fun obtainEvent(event: NotificationStateEvent) {
        when (event) {
            is NotificationStateEvent.ClearAllNotification -> openDialogClearingOrPermission()
            is NotificationStateEvent.Default -> setEvent(NotificationStateEvent.Default)
            is NotificationStateEvent.Update -> updateAppsAndService()
            is NotificationStateEvent.SwitchGeneralDisturbMode -> switchGeneralDisturbMode(event.isSwitch)
            is NotificationStateEvent.LimitApps -> setToAllAppsModeDisturb(event.isSwitch)
            is NotificationStateEvent.CloseDialogClearing -> clearAllNotification(event)
            is NotificationStateEvent.OpenDialogCompleteClean -> setEvent(event)
            is NotificationStateEvent.SwitchAppModeDisturb -> switchAppModeDisturb(event.packageName, event.isSwitch)
            is NotificationStateEvent.OpenMissedNotification -> setEvent(event)
            else -> {}
        }
    }

    private fun getAppWithNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getAppsInfo().collect { listApps ->
                updateState {
                    it.copy(
                        apps = listApps.mapToAppUiList(useCases.hasServicePermission()),
                        event = NotificationStateEvent.Default
                    )
                }
            }
        }
    }

    private fun clearAllNotification(event: NotificationStateEvent) {
        useCases.clearAllNotification()
        setEvent(event)
    }

    private fun openDialogClearingOrPermission() {
        if (useCases.hasServicePermission()) {
            setEvent(NotificationStateEvent.OpenDialogClearing)
        } else {
            setEvent(NotificationStateEvent.OpenPermissionDialog)
        }
    }

    private fun setEvent(event: NotificationStateEvent) {
        updateState {
            it.copy(
                event = event
            )
        }
    }

    private fun switchGeneralDisturbMode(isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            if (useCases.hasServicePermission()) {
                useCases.setGeneralDisturbMode(isSwitched)
                updateState {
                    it.copy(
                        modeNotDisturb = isSwitched,
                        event = NotificationStateEvent.Default
                    )
                }
            } else {
                setEvent(NotificationStateEvent.OpenPermissionDialog)
                updateState {
                    it.copy(
                        modeNotDisturb = !isSwitched,
                    )
                }
            }
        }
    }

    private fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) {
        if (useCases.hasServicePermission()) {
            viewModelScope.launch(Dispatchers.Default) {
                useCases.switchAppModeDisturb(packageName, isSwitched)
            }
        } else {
            setEvent(NotificationStateEvent.OpenPermissionDialog)
        }
    }

    private fun updateAppsAndService() {
        getAppWithNotifications()
        updateApps()
        updateDisturbSettings()
        useCases.startServiceIfNeed()
    }

    private fun setToAllAppsModeDisturb(isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (useCases.hasServicePermission()) {
                updateState {
                    it.copy(
                        isAllAppsLimited = isSwitched,
                        event = NotificationStateEvent.Default
                    )
                }
                useCases.switchModeDisturbForAllApps(isSwitched)
            } else {
                setEvent(NotificationStateEvent.OpenPermissionDialog)
                updateState {
                    it.copy(
                        isAllAppsLimited = !isSwitched,
                    )
                }
            }
        }
    }

    private fun updateDisturbSettings() {
        viewModelScope.launch(Dispatchers.Default) {
            updateState {
                it.copy(
                    modeNotDisturb = useCases.getDisturbMode(),
                    isAllAppsLimited = useCases.isAllAppsLimited(),
                    event = NotificationStateEvent.Default
                )
            }
        }
    }

    private fun updateApps() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateApps()
        }
    }

}