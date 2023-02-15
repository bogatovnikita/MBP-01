package com.entertainment.event.ssearch.presentation.ui.notification_manager

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.use_cases.DNDSettingsUseCase
import com.entertainment.event.ssearch.domain.use_cases.NotificationSettingsUseCases
import com.entertainment.event.ssearch.presentation.R
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
    private val useCasesDND: DNDSettingsUseCase,
) : BaseViewModel<NotificationSettingsState>(NotificationSettingsState()) {

    fun obtainEvent(event: NotificationStateEvent) {
        when (event) {
            is NotificationStateEvent.ClearAllNotification -> openDialogClearingOrPermission()
            is NotificationStateEvent.Default -> setEvent(NotificationStateEvent.Default)
            is NotificationStateEvent.Update -> {
                updateAppsAndService()
                updateTimetableState()
            }
            is NotificationStateEvent.SwitchGeneralDisturbMode -> switchGeneralDisturbMode(event.isSwitch)
            is NotificationStateEvent.LimitApps -> setToAllAppsModeDisturb(event.isSwitch)
            is NotificationStateEvent.CloseDialogClearing -> clearAllNotification(event)
            is NotificationStateEvent.OpenDialogCompleteClean -> setEvent(event)
            is NotificationStateEvent.SwitchAppModeDisturb -> switchAppModeDisturb(event.packageName, event.isSwitch)
            is NotificationStateEvent.OpenMissedNotification -> setEvent(event)
            is NotificationStateEvent.OpenTimeTable -> openTimeTable(event)
            else -> {}
        }
    }

    private fun checkIsAllAppsLimited() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.checkAllAppsLimited().collect{ isLimited ->
                updateState {
                    it.copy(
                        isAllAppsLimited = !isLimited
                    )
                }
            }
        }
    }

    private fun updateTimetableState() {
        viewModelScope.launch {
            val isAutoModeSwitched = useCasesDND.isAutoModeSwitched()
            val selectedDays = useCasesDND.getSelectedDays().map(::toResId)
            val isNeedShowTimetableInfo = isAutoModeSwitched || selectedDays.isNotEmpty() || isAutoModeSwitched && useCasesDND.isOnlyToday()
            updateState {
                it.copy(
                    isAutoModeEnable = isAutoModeSwitched,
                    timeStart = useCasesDND.getStartTime(),
                    timeEnd = useCasesDND.getEndTime(),
                    selectedDays = selectedDays,
                    isNeedShowTimetableInfo = isNeedShowTimetableInfo,
                )
            }
        }
        setEvent(NotificationStateEvent.Default)
    }

    private fun toResId(day: Int): Int {
        return when (day) {
            DNDSettingsUseCase.MONDAY -> R.string.notification_manager_mon
            DNDSettingsUseCase.TUESDAY -> R.string.notification_manager_tu
            DNDSettingsUseCase.WEDNESDAY -> R.string.notification_manager_we
            DNDSettingsUseCase.THURSDAY -> R.string.notification_manager_th
            DNDSettingsUseCase.FRIDAY -> R.string.notification_manager_fr
            DNDSettingsUseCase.SATURDAY -> R.string.notification_manager_sat
            DNDSettingsUseCase.SUNDAY -> R.string.notification_manager_sun
            else -> R.string.notification_manager_mon
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

    private fun openTimeTable(event: NotificationStateEvent) {
        if (useCases.hasServicePermission()) {
            setEvent(event)
        } else {
            setEvent(NotificationStateEvent.OpenServicePermissionDialog)
        }
    }

    private fun openDialogClearingOrPermission() {
        if (!useCases.hasServicePermission()) {
            setEvent(NotificationStateEvent.OpenServicePermissionDialog)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !useCases.hasNotificationPermission()) {
            setEvent(NotificationStateEvent.OpenNotificationPermissionDialog)
        } else {
            setEvent(NotificationStateEvent.OpenDialogClearing)
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
                        modeDND = isSwitched,
                        event = NotificationStateEvent.Default
                    )
                }
            } else {
                setEvent(NotificationStateEvent.OpenServicePermissionDialog)
                updateState {
                    it.copy(
                        modeDND = !isSwitched,
                    )
                }
            }
        }
    }

    private fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) {
        if (useCases.hasServicePermission()) {
            viewModelScope.launch(Dispatchers.Default) {
                useCases.switchAppModeDisturb(packageName, isSwitched)
                checkIsAllAppsLimited()
            }
        } else {
            setEvent(NotificationStateEvent.OpenServicePermissionDialog)
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
                setEvent(NotificationStateEvent.LimitApps(isSwitched))
                updateState {
                    it.copy(
                        isAllAppsLimited = isSwitched
                    )
                }
                useCases.switchModeDisturbForAllApps(isSwitched)
            } else {
                setEvent(NotificationStateEvent.OpenServicePermissionDialog)
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
                    modeDND = useCases.getDisturbMode(),
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