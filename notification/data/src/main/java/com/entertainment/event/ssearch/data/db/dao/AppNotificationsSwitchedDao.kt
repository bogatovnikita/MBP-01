package com.entertainment.event.ssearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.entertainment.event.ssearch.data.db.entity.AppNotificationsSwitchedEntity

@Dao
interface AppNotificationsSwitchedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun write(switched: AppNotificationsSwitchedEntity)

    @Query("SELECT isSwitched FROM AppNotificationsSwitchedEntity WHERE packageName = :packageName")
    fun isSwitched(packageName: String) : Boolean

    @Query("UPDATE AppNotificationsSwitchedEntity SET isSwitched = :switched WHERE packageName = :packageName")
    fun setSwitched(packageName: String, switched: Boolean)

}