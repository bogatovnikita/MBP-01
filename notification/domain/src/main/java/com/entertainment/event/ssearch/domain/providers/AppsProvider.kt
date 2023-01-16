package com.entertainment.event.ssearch.domain.providers

import android.content.pm.PackageInfo

interface AppsProvider {

    suspend fun getInstalledPackages() : List<PackageInfo>

    suspend fun getSystemPackages() : List<PackageInfo>

}