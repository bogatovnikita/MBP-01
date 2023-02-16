package yin_kio.acceleration.data

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import yin_kio.acceleration.domain.gateways.Apps
import java.util.concurrent.TimeUnit

class AndroidApps(
    private val context: Context
) : Apps{

    override suspend fun provide(): List<String> {
        val stats = getStats()

        return stats.map { it.packageName }
            .filter {
                it != context.packageName
                && context.packageManager.getLaunchIntentForPackage(it) != null
                && !it.contains(".test")
            }
    }

    private fun getStats(): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - TimeUnit.MINUTES.toMillis(1),
            currentTime
        ) ?: listOf()
    }

    override suspend fun stop(apps: Collection<String>) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        apps.forEach { packageName ->
            runCatching {
                activityManager.killBackgroundProcesses(packageName)
            }
        }
    }
}