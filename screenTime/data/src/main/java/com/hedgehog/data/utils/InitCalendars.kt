package com.hedgehog.data.utils

import java.util.*

class InitCalendars() {

    fun initBeginTime(): Calendar {
        val beginTime = Calendar.getInstance()
        beginTime.set(Calendar.HOUR_OF_DAY, 0)
        beginTime.set(Calendar.MINUTE, 0)
        beginTime.set(Calendar.SECOND, 0)
        beginTime.set(Calendar.MILLISECOND, 0)
        return beginTime
    }

    fun initEndTime(): Calendar {
        val endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY, 0)
        endTime.set(Calendar.MINUTE, 0)
        endTime.set(Calendar.SECOND, -1)
        endTime.set(Calendar.MILLISECOND, 0)
        return endTime
    }

    companion object {
        const val ONE_WEEK = 7
        const val SIX_DAYS = 6
        const val ONE_DAY = 1

        const val HOURS_IN_DAY = 23
        const val ONE_HOUR = 1

        const val ONE_HOUR_MILLISECONDS = 3_600_000L
        const val ONE_DAY_MILLISECONDS = 86_400_000L

    }
}