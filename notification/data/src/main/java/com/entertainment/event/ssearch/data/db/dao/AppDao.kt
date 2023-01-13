package com.entertainment.event.ssearch.data.db.dao

import androidx.room.*
import com.entertainment.event.ssearch.data.db.entity.App
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertApp(app: App)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(apps: List<App>)

    @Query("SELECT * FROM app_table")
    suspend fun readAll() : Flow<List<App>>

    @Query("UPDATE app_table SET isSwitched = :switched WHERE packageName = :packageName")
    suspend fun setSwitched(packageName: String, switched: Boolean)

    @Update
    suspend fun updateAll(apps: List<App>)

}