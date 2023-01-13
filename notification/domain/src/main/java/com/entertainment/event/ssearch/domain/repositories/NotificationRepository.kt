package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.NotificationDomain
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun readAll(): Flow<List<NotificationDomain>>

    suspend fun delete(notificationId: Int)

    suspend fun deleteAll()

}