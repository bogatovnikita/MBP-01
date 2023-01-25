package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.NotificationsDao
import com.entertainment.event.ssearch.domain.models.Notification
import com.entertainment.event.ssearch.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Notifications @Inject constructor(
    private val db: NotificationsDao,
): NotificationRepository {

    override suspend fun readAll(): Flow<List<Notification>> =
        db.readAll().map { notifications -> notifications.mapToNotification() }

    override suspend fun delete(time: Long) = db.delete(time)

    override suspend fun deleteAll() = db.deleteAll()

    override suspend fun insert(notification: Notification) = db.insert(notification.mapToNotification())

}