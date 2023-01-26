package com.hedgehog.presentation.models

import java.io.Serializable

data class CalendarScreenTime(
    val dataType: Int = 0,
    val beginTime: Int = 0,
    val endTime: Int = 0
) : Serializable