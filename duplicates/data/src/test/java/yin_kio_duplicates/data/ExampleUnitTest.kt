package yin_kio_duplicates.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import yin_kio.duplicates.domain.findDuplicates

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @Test
    fun `test image duplicates`() {
        val pixel1 = "src/test/res/pixel.png"
        val pixel2 = "src/test/res/pixel_2.webp"


        val a = BitmapFactory.decodeFile(pixel1)
        val b = BitmapFactory.decodeFile(pixel1)
        val c = BitmapFactory.decodeFile(pixel2)
        val d = BitmapFactory.decodeFile(pixel2)
        val e = Bitmap.createBitmap(3, 1, Bitmap.Config.ARGB_8888)
        val f = Bitmap.createBitmap(1, 3, Bitmap.Config.ARGB_8888)
        val expected = listOf(a,b,c,d)
        val actual = listOf(a,b,c,d,e,f).findDuplicates { a, b -> a.sameAs(b) }
        assertEquals(expected, actual)
    }
}