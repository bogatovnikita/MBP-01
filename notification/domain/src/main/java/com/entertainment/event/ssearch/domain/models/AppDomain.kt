package com.entertainment.event.ssearch.domain.models

data class AppDomain(
    val packageName: String,
    val list: List<NotificationDomain>,
    val isSwitched: Boolean,
)