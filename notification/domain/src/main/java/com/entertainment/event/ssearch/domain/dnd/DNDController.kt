package com.entertainment.event.ssearch.domain.dnd

interface DNDController {

    suspend fun setDNDModeOn()

    suspend fun setDNDModeOff()

    suspend fun isDNDModeOff(): Boolean
}