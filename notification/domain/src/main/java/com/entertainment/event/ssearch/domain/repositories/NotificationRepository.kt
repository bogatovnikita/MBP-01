package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun readAll(): Flow<List<Notification>>

    suspend fun delete(time: Long)

    suspend fun deleteAll()

    suspend fun insert(notification: Notification)

}