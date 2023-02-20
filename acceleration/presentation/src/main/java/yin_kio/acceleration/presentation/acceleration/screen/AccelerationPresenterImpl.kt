package yin_kio.acceleration.presentation.acceleration.screen

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut

class AccelerationPresenterImpl(
    private val applicationContext: Context
) : AccelerationPresenter {

    override fun presentRamInfoOut(ramInfoOut: RamInfoOut): RamInfo {

        return RamInfo(
            progress = (ramInfoOut.occupied.toDouble() / ramInfoOut.total).toFloat(),
            occupied = formatSize(ramInfoOut.occupied),
            available = formatSize(ramInfoOut.available),
            total = formatSize(ramInfoOut.total)
        )
    }

    private fun formatSize(size: Long) : String{
        return formatFileSize(applicationContext, size)
    }
}