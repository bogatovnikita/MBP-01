package yin_kio.memory.data

import android.app.ActivityManager
import android.app.ActivityManager.MemoryInfo
import android.content.Context
import yin_kio.memory.domain.MemoryInfoOut
import yin_kio.memory.domain.RamInfo

class RamInfoProvider(
    private val context: Context
) : RamInfo {

    override suspend fun provide(): MemoryInfoOut {
        val memInfo = MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memInfo)

        val occupied = memInfo.totalMem - memInfo.availMem
        val available = memInfo.availMem
        val total = memInfo.totalMem

        return MemoryInfoOut(
            occupied = occupied,
            available = available,
            total = total
        )

    }
}