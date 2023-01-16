package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.domain.models.AppDomain
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val db: AppDao,
): AppRepository {

    override suspend fun insertApp(app: AppDomain) = db.insertApp(app.mapToApp())

    override suspend fun insertAll(apps: List<AppDomain>) = db.insertAll(apps.map { app -> app.mapToApp() })

    override suspend fun readAll(): Flow<List<AppDomain>> = db.readAll().map { it.map { it.mapToAppDomain() } }

    override suspend fun setSwitched(packageName: String, switched: Boolean) = db.setSwitched(packageName, switched)

    override suspend fun updateAll(apps: List<AppDomain>) = db.updateAll(apps.map { app -> app.mapToApp() })

}