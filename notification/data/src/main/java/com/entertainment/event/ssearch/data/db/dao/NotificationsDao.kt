package com.entertainment.event.ssearch.data.db.dao

import androidx.room.*
import com.entertainment.event.ssearch.data.db.entity.NotificationEntity

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun write(notification: NotificationEntity)

    @Query("SELECT * FROM NotificationEntity")
    fun readAll() : List<NotificationEntity>

    @Query("DELETE FROM NotificationEntity WHERE packageName = :packageName AND body = :body")
    fun delete(packageName: String, body: String)

    @Query("SELECT COUNT(*) FROM NotificationEntity WHERE packageName = :packageName")
    fun notificationsCount(packageName: String) : Int

    @Update
    fun update(notificationEntity: NotificationEntity)

}