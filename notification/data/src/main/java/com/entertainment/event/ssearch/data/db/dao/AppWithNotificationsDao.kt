package com.entertainment.event.ssearch.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.entertainment.event.ssearch.data.db.entity.AppWithNotificationsDb
import kotlinx.coroutines.flow.Flow

@Dao
interface AppWithNotificationsDao {

    @Transaction
    @Query("SELECT * FROM app_table")
    fun readAppsWithNotifications(): Flow<List<AppWithNotificationsDb>>

}