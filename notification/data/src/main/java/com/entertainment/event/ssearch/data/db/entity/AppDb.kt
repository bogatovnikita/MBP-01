package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_table")
data class AppDb(
    @PrimaryKey (autoGenerate = false)
    val packageName: String,
    val icon: String,
    val name: String,
    val isSwitched: Boolean
)
