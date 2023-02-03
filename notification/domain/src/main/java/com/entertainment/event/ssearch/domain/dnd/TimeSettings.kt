package com.entertainment.event.ssearch.domain.dnd

interface TimeSettings {

    suspend fun getStartTime(): Int

    suspend fun setStartTime(time: Int)

    suspend fun getEndTime(): Int

    suspend fun setEndTime(time: Int)

}