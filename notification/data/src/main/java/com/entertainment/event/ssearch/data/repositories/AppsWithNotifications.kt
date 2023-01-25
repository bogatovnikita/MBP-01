package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.domain.models.AppWithNotifications
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppsWithNotifications @Inject constructor(
    private val db: AppWithNotificationsDao,
) : AppWithNotificationsRepository {

    override suspend fun readAppsWithNotifications(): Flow<List<AppWithNotifications>> =
        db.readAppsWithNotifications().map { listAppWithNotificationsDb ->
            listAppWithNotificationsDb.mapToAppAppWithNotifications()
        }

}