package com.entertainment.event.ssearch.data.db.dao

import androidx.room.*
import com.entertainment.event.ssearch.data.db.entity.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notification_table")
    suspend fun readAll() : Flow<List<Notification>>

    @Query("DELETE FROM notification_table WHERE notificationId = :notificationId")
    suspend fun delete(notificationId: Int)

    @Query("DELETE FROM notification_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(notification: Notification)

}