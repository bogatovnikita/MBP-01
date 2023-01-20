package com.entertainment.event.ssearch.data.db.dao

import androidx.room.*
import com.entertainment.event.ssearch.data.db.entity.AppDb
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertApp(appDb: AppDb)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(appDbs: List<AppDb>)

    @Query("SELECT * FROM app_table")
    fun readAll() : Flow<List<AppDb>>

    @Query("SELECT * FROM app_table")
    fun getApps() : List<AppDb>

    @Query("SELECT * FROM app_table WHERE packageName = :packageName")
    suspend fun readApp(packageName : String) : AppDb

    @Query("UPDATE app_table SET isSwitched = :switched WHERE packageName = :packageName")
    suspend fun setSwitched(packageName: String, switched: Boolean)

    @Update
    suspend fun updateAll(appDbs: List<AppDb>)

}