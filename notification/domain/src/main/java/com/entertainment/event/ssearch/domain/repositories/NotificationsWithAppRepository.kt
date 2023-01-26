package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.NotificationWithApp
import kotlinx.coroutines.flow.Flow

interface NotificationsWithAppRepository {

    suspend fun readNotificationsWithApp(): Flow<List<NotificationWithApp>>

}