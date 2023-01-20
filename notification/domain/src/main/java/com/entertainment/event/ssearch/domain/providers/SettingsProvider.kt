package com.entertainment.event.ssearch.domain.providers

interface SettingsProvider {

    suspend fun switchOffDisturbMode(isSwitched: Boolean)

    suspend fun isDisturbModeSwitched(): Boolean

}