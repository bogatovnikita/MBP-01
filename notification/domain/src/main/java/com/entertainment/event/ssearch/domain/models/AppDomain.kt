package com.entertainment.event.ssearch.domain.models

data class AppDomain(
    val name: String,
    val icon: Int,
    val packageName: String,
    val list: List<NotificationDomain>,
    val isSwitched: Boolean,
)