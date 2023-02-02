package com.entertainment.event.ssearch.domain.dnd

interface Settings {

    suspend fun setDisturbMode(isSwitched: Boolean)

    suspend fun isDisturbModeEnabled(): Boolean

    suspend fun setLimitAllApps(isSwitched: Boolean)

    suspend fun isAllAppsLimited(): Boolean

    suspend fun setAutoModeDND(isSwitched: Boolean)

    suspend fun isAutoModeDNDEnabled(): Boolean

}