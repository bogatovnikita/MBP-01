package yin_kio.acceleration.data

import android.app.ActivityManager
import android.content.Context
import yin_kio.acceleration.domain.acceleration.RamInfo
import yin_kio.acceleration.domain.acceleration.RamInfoOut


// Дублируется в yin_kio.memory.data
class RamInfoProvider(
    private val context: Context
) : RamInfo {

    override fun provide(): RamInfoOut {
        val memInfo = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memInfo)

        val occupied = memInfo.totalMem - memInfo.availMem
        val available = memInfo.availMem
        val total = memInfo.totalMem

        return RamInfoOut(
            occupied = occupied,
            available = available,
            total = total
        )

    }
}