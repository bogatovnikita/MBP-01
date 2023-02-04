package com.entertainment.event.ssearch.presentation.extensions

fun Int.toTime(): String = "${String.format("%02d", this / 60)}:${String.format("%02d", this % 60)}"

fun Int.toHours() = this / 60

fun Int.toMinutes() = this % 60