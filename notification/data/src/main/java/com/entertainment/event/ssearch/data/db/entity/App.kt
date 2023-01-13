package com.entertainment.event.ssearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_table")
data class App(
    @PrimaryKey (autoGenerate = false)
    val packageName: String,
    val isSwitched: Boolean
)
