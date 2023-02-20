package com.entertainment.event.ssearch.data.repositories

import com.entertainment.event.ssearch.data.db.dao.AppDao
import com.entertainment.event.ssearch.domain.models.App
import com.entertainment.event.ssearch.domain.repositories.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Apps @Inject constructor(
    private val db: AppDao,
): AppRepository {

    override suspend fun insertApp(app: App) = db.insertApp(app.mapToAppDb())

    override suspend fun insertAll(apps: List<App>) = db.insertAll(apps.map { app -> app.mapToAppDb() })

    override suspend fun readAll(): Flow<List<App>> = db.readAll().map { appDb -> appDb.map { it.mapToApp() } }

    override suspend fun getApps(): List<App> = db.getApps().map { it.mapToApp() }

    override suspend fun deleteApp(packageName: String) = db.deleteApp(packageName)

    override suspend fun readApp(packageName: String): App? = db.readApp(packageName)?.mapToApp()

    override suspend fun setNotificationDisabled(packageName: String, switched: Boolean) = db.setSwitched(packageName, switched)

    override suspend fun updateAll(apps: List<App>) = db.updateAll(apps.map { app -> app.mapToAppDb() })

}