package com.entertainment.event.ssearch.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.entertainment.event.ssearch.data.db.entity.NotificationWithAppDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsWithAppDao {

    @Transaction
    @Query("SELECT * FROM notification_table")
    fun readNotificationWithApp(): Flow<List<NotificationWithAppDb>>

}