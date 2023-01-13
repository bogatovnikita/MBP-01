package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import com.entertainment.event.ssearch.domain.models.NotificationDomain
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val db: NotificationsDao,
): NotificationRepository {

    override suspend fun readAll(): Flow<List<NotificationDomain>> =
        db.readAll().map { notifications -> notifications.mapToNotificationDomain() }

    override suspend fun delete(notificationId: Int) = db.delete(notificationId)

    override suspend fun deleteAll() = db.deleteAll()

}