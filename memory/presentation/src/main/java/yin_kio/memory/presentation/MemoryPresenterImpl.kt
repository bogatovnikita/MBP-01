package yin_kio.memory.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.memory.domain.MemoryInfoOut

class MemoryPresenterImpl(
    private val context: Context
) : MemoryPresenter {

    override suspend fun presentMemoryState(memoryInfoOut: MemoryInfoOut): MemoryState {
        return MemoryState(
            progress = (memoryInfoOut.occupied.toDouble() / memoryInfoOut.total).toFloat(),
            occupied = formatFileSize(context, memoryInfoOut.occupied),
            available = formatFileSize(context, memoryInfoOut.available),
            total = formatFileSize(context, memoryInfoOut.total)
        )
    }
}