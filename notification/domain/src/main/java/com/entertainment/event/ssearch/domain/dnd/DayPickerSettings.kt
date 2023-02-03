package com.entertainment.event.ssearch.domain.dnd

interface DayPickerSettings {

    suspend fun setMondayInclude(isSwitched: Boolean)

    suspend fun isMondayIncluded(): Boolean

    suspend fun setTuesdayInclude(isSwitched: Boolean)

    suspend fun isTuesdayIncluded(): Boolean

    suspend fun setWednesdayInclude(isSwitched: Boolean)

    suspend fun isWednesdayIncluded(): Boolean

    suspend fun setThursdayInclude(isSwitched: Boolean)

    suspend fun isThursdayIncluded(): Boolean

    suspend fun setFridayInclude(isSwitched: Boolean)

    suspend fun isFridayIncluded(): Boolean

    suspend fun setSaturdayInclude(isSwitched: Boolean)

    suspend fun isSaturdayIncluded(): Boolean

    suspend fun setSundayInclude(isSwitched: Boolean)

    suspend fun isSundayIncluded(): Boolean

}