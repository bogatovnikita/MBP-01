package yin_kio.garbage_clean.data

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider

class FileSystemInfoProviderImpl(
    private val context: Context
) : FileSystemInfoProvider {

    override suspend fun getFileSystemInfo(): FileSystemInfo {
        // Здесь получение инфы дублируется с модулем memory:data.
        // Если этот код появится ещё где-то, рекоменудую создать для него отдельный модуль
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val storageStatsManager =  context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val total = storageStatsManager.getTotalBytes(StorageManager.UUID_DEFAULT)
            val free = storageStatsManager.getFreeBytes(StorageManager.UUID_DEFAULT)
            val occupied = total - free

            FileSystemInfo(
                occupied = occupied,
                total = total,
                available = free
            )
        } else {
            val stats = StatFs(Environment.getExternalStorageDirectory().absolutePath)
            val total = stats.totalBytes
            val free = stats.freeBytes
            val occupied = total - free
            FileSystemInfo(
                occupied = occupied,
                total = total,
                available = free
            )
        }
    }
}