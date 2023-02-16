package yin_kio.acceleration.data

import android.app.usage.UsageStatsManager
import android.content.Context
import yin_kio.acceleration.domain.gateways.Apps
import java.util.*

class AndroidApps(
    private val context: Context
) : Apps{

    override fun provide(): List<String> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = Calendar.getInstance().timeInMillis
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - 60 * 1000,
            currentTime
        )
        return stats.map { it.packageName }
    }
}