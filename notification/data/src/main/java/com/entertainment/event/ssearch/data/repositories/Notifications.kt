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
        db.readAll().map { notifications -> notifications.mapToNotificationDb() }

    override suspend fun delete(notification: Notification) = db.delete(notification.mapToNotificationDb())

    override suspend fun deleteAll() = db.deleteAll()

    override suspend fun insert(notification: Notification) = db.insert(notification.mapToNotificationDb())

}