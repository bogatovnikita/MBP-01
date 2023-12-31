package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import kotlinx.coroutines.flow.Flow

interface AppWithNotificationsRepository {

    suspend fun readAppsWithNotifications(): Flow<List<AppWithNotifications>>

}