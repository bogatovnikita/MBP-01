package com.entertainment.event.ssearch.domain.models

data class AppWithNotifications(
    val app: App,
    val listNotifications: List<Notification>,
)