package yin_kio.memory.presentation

import android.text.format.Formatter.formatFileSize
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.memory.domain.MemoryInfoOut

@RunWith(RobolectricTestRunner::class)
class MemoryPresenterTest {

    private val context = RuntimeEnvironment.getApplication()

    private val presenter = MemoryPresenterImpl(context)

    @Test
    fun testPresentMemoryState() = runBlocking{


        val input = MemoryInfoOut(
            occupied = 500,
            available = 500,
            total = 1000,
        )

        val output = MemoryState(
            progress = 0.5f,
            occupied = formatFileSize(context, 500),
            available = formatFileSize(context, 500),
            total = formatFileSize(context, 1000)
        )

        assertEquals(output, presenter.presentMemoryState(input))

    }
}