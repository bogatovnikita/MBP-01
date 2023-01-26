package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.NotificationsWithAppDao
import com.entertainment.event.ssearch.domain.models.NotificationWithApp
import com.entertainment.event.ssearch.domain.repositories.NotificationsWithAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationsWithApp @Inject constructor(
    private val db: NotificationsWithAppDao
) : NotificationsWithAppRepository {
    override suspend fun readNotificationsWithApp(): Flow<List<NotificationWithApp>> =
        db.readNotificationWithApp().map { notifications -> notifications.map { it.toNotificationWithApp() } }
}