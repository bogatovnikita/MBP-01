package com.entertainment.event.ssearch.presentation.ui.notification_manager

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.notification_manager_settings.NotificationSettingsUseCases
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import com.entertainment.event.ssearch.presentation.ui.mappers.mapToAppUiList
import com.entertainment.event.ssearch.presentation.ui.models.NotificationSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val useCases: NotificationSettingsUseCases,
) : BaseViewModel<NotificationSettingsState>(NotificationSettingsState()) {

    init {
        updateDisturbSettings()
    }

    fun getAppWithNotifications(hasPermission: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getAppsInfo(hasPermission).collect { listApps ->
                updateState {
                    it.copy(
                        apps = listApps.mapToAppUiList()
                            .sortedWith(compareBy({ it.isSwitched }, { it.lastNotificationTime }))
                            .reversed()
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateApps(hasPermission)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun switchGeneralDisturbMode(isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            updateState {
                it.copy(
                    modeNotDisturb = isSwitched
                )
            }
            useCases.setGeneralDisturbMode(isSwitched)
        }
    }

    fun switchAppModeDisturb(packageName: String, isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            useCases.switchAppModeDisturb(packageName, isSwitched)
        }
    }

    fun setToAllAppsModeDisturb(isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                it.copy(
                    isAllAppsLimited = isSwitched
                )
            }
            useCases.switchModeDisturbForAllApps(isSwitched)
        }
    }

    private fun updateDisturbSettings() {
        viewModelScope.launch(Dispatchers.Default) {
            updateState {
                it.copy(
                    modeNotDisturb = useCases.getDisturbMode(),
                    isAllAppsLimited = useCases.isAllAppsLimited()
                )
            }
        }
    }

    private fun updateApps(hasPermission: Boolean) {
        if (!hasPermission) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCases.updateApps()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}