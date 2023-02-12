package com.entertainment.event.ssearch.domain.dnd

interface DNDAutoModeController {

    fun setRepeatAlarm(
        hours: Int,
        action: Int,
        minutes: Int,
        daysOfWeek: List<Int>,
    )

    fun setSingleAlarm(
        day: Int,
        hours: Int,
        action: Int,
        minutes: Int,
    )

}