package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.AppWithNotificationsDao
import com.entertainment.event.ssearch.domain.models.AppDomain
import com.entertainment.event.ssearch.domain.repositories.AppWithNotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppWithNotificationsRepoImpl @Inject constructor(
    private val db: AppWithNotificationsDao,
) : AppWithNotificationsRepository {

    override suspend fun readAppsWithNotifications(): Flow<List<AppDomain>> =
        db.readAppsWithNotifications().map { listAppWithNotifications ->
            listAppWithNotifications.mapToAppDomain()
        }

}