package com.entertainment.event.ssearch.domain.repositories

import com.entertainment.event.ssearch.domain.models.AppDomain
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun insertApp(app: AppDomain)

    suspend fun insertAll(apps: List<AppDomain>)

    suspend fun readAll(): Flow<List<AppDomain>>

    suspend fun setSwitched(packageName: String, switched: Boolean)

    suspend fun updateAll(apps: List<AppDomain>)

}