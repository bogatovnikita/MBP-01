package yin_kio.memory.data

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import yin_kio.memory.domain.MemoryInfoOut
import yin_kio.memory.domain.StorageInfo

class StorageInfoProvider(
    private val applicationContext: Context
)  : StorageInfo{

    override fun provide() : MemoryInfoOut {
        // Здесь получение инфы дублируется с модулем garbage_clean:data.
        // Если этот код появится ещё где-то, рекоменудую создать для него отдельный модуль

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val storageStatsManager =  applicationContext.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val total = storageStatsManager.getTotalBytes(StorageManager.UUID_DEFAULT)
            val free = storageStatsManager.getFreeBytes(StorageManager.UUID_DEFAULT)
            val occupied = total - free

            MemoryInfoOut(
                occupied = occupied,
                total = total,
                available = free
            )
        } else { // Для ранних версий андроид использование StatFs для получения объёма памяти, вероятно,
            // не будет давать полный объём хранилища (32, 64 гб и т. п.). Вместо этого,
            // скорее всего, будет возвращён объём немного меньше. Советую проверить.
            val stats = StatFs(Environment.getExternalStorageDirectory().absolutePath)
            val total = stats.totalBytes
            val free = stats.freeBytes
            val occupied = total - free
            MemoryInfoOut(
                occupied = occupied,
                total = total,
                available = free
            )
        }

    }
}