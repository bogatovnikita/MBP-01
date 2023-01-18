package com.entertainment.event.ssearch.data.db.dao

import androidx.room.*
import com.entertainment.event.ssearch.data.db.entity.NotificationDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificationDb: NotificationDb)

    @Query("SELECT * FROM notification_table")
    fun readAll() : Flow<List<NotificationDb>>

    @Query("DELETE FROM notification_table WHERE notificationId = :notificationId")
    suspend fun delete(notificationId: Int)

    @Query("DELETE FROM notification_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(notificationDb: NotificationDb)

}