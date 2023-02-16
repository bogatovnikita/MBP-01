package yin_kio.acceleration.data

import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import yin_kio.acceleration.domain.gateways.Apps
import java.util.*
import java.util.concurrent.TimeUnit

class AndroidApps(
    private val context: Context
) : Apps{

    override fun provide(): List<String> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = Calendar.getInstance().timeInMillis
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - TimeUnit.MINUTES.toMillis(1),
            currentTime
        )
        return stats.map { it.packageName }
            .filter { it != context.packageName } // Лучше бы это перенести в домен
    }

    override fun stop(apps: Collection<String>) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        apps.forEach { packageName ->
            runCatching {
                activityManager.killBackgroundProcesses(packageName)
            }
        }
    }
}