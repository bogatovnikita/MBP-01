package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun insertApp(app: App)

    suspend fun insertAll(apps: List<App>)

    suspend fun readAll(): Flow<List<App>>

    suspend fun getApps(): List<App>

    suspend fun readApp(packageName: String): App

    suspend fun setSwitched(packageName: String, switched: Boolean)

    suspend fun updateAll(apps: List<App>)

}