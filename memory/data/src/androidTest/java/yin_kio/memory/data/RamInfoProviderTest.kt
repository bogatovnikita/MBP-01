package yin_kio.memory.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RamInfoProviderTest {
    @Test
    fun testRamInfoProvider() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val provider = RamInfoProvider(context)

        val memOut = provider.provide()

        assertTrue(
            memOut.total > 0
            && memOut.available > 0
            && memOut.occupied > 0
        )
    }
}