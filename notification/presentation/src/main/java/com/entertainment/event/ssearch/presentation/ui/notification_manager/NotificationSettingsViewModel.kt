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

    fun getAppWithNotifications(hasPermission: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
                useCases.getAppsInfo(hasPermission).collect { listApps ->
                    updateState {
                        it.copy(
                            apps = listApps.mapToAppUiList()
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

    fun switchModeDisturb(packageName: String, isSwitched: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            useCases.switchModeDisturb(packageName, isSwitched)
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