package yin_kio.acceleration.presentation

import android.text.format.Formatter.formatFileSize
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.presentation.acceleration.AccelerationPresenter
import yin_kio.acceleration.presentation.acceleration.AccelerationPresenterImpl
import yin_kio.acceleration.presentation.acceleration.RamInfo


@RunWith(RobolectricTestRunner::class)
class AccelerationPresenterTest {

    private val context = RuntimeEnvironment.getApplication()
    private val presenter: AccelerationPresenter = AccelerationPresenterImpl(context)

    @Test
    fun testPresentRamInfoOut(){
        assertSize(0f, 0, 1000, 1000)
        assertSize(0.5f, 1000, 1000, 2000)
        assertSize(1f, 2000, 0, 2000)
    }

    private fun assertSize(
        progress: Float,
        occupied: Long,
        available: Long,
        total: Long
    ){
        val empty = RamInfo(
            progress = progress,
            occupied = formatFileSize(context, occupied),
            available = formatFileSize(context, available),
            total = formatFileSize(context, total)
        )

        val res = presenter.presentRamInfoOut(RamInfoOut(
            occupied = occupied,
            available = available,
            total = total
        ))

        assertEquals(empty, res)
    }

}