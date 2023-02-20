package com.entertainment.event.ssearch.data.providers

import android.app.Application
import android.content.pm.ApplicationInfo
import com.entertainment.event.ssearch.domain.mappers.mapToApp
import com.entertainment.event.ssearch.domain.models.App
import com.entertainment.event.ssearch.domain.providers.AppsProvider
import javax.inject.Inject

class AppsProviderImpl @Inject constructor(
    private val context: Application
): AppsProvider {

    override suspend fun getInstalledApp() : List<App> {
        return context.packageManager.getInstalledPackages(0).filter {
            it.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 0 &&
                    it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 &&
                    !it.packageName.contains(".test")
        }.mapToApp(context)
    }

    override suspend fun getSystemApp() : List<App> {
        return context.packageManager.getInstalledPackages(0).filter {
            it.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0 &&
                    it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 &&
                    !it.packageName.contains(".test")
        }.mapToApp(context)
    }

}