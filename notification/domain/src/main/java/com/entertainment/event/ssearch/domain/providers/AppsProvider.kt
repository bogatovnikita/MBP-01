package com.entertainment.event.ssearch.domain.providers

import com.entertainment.event.ssearch.domain.models.App

interface AppsProvider {

    suspend fun getInstalledApp() : List<App>

    suspend fun getSystemApp() : List<App>

}