package com.entertainment.event.ssearch.presentation.ui.notification_manager

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.notification_manager_settings.NotificationSettingsUseCases
import com.entertainment.event.ssearch.presentation.ui.base.BaseViewModel
import com.entertainment.event.ssearch.presentation.ui.models.NotificationSettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class NotificationSettingsViewModel @Inject constructor(
    private val useCases: NotificationSettingsUseCases
) : BaseViewModel<NotificationSettingsState>(NotificationSettingsState()) {

    private fun getAppWithNotifications(hasPermission: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCases.getAppsInfo(hasPermission).collect { listApps ->
                    updateState {
                        it.copy(
                            apps = listApps
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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