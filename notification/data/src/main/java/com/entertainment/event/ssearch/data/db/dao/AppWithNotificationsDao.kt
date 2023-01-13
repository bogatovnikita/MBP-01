package com.entertainment.event.ssearch.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.entertainment.event.ssearch.data.db.entity.AppWithNotifications
import kotlinx.coroutines.flow.Flow

@Dao
interface AppWithNotificationsDao {

    @Transaction
    @Query("SELECT * FROM app_table")
    suspend fun readAppsWithNotifications(): Flow<List<AppWithNotifications>>

}