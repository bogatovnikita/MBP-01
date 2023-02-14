package com.entertainment.event.ssearch.domain.permission

interface Permission {

    fun hasServicePermission(): Boolean

    fun hasNotificationPermission(): Boolean

}