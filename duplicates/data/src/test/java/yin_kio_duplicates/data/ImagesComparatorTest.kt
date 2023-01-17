package yin_kio_duplicates.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import yin_kio.duplicates.domain.findDuplicates
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.models.ImageInfo

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ImagesComparatorTest {
    @Test
    fun `learn test`() {


        val a = BitmapFactory.decodeFile(PATH_1)
        val b = BitmapFactory.decodeFile(PATH_1)
        val c = BitmapFactory.decodeFile(PATH_2)
        val d = BitmapFactory.decodeFile(PATH_2)
        val e = Bitmap.createBitmap(3, 1, Bitmap.Config.ARGB_8888)
        val f = Bitmap.createBitmap(1, 3, Bitmap.Config.ARGB_8888)
        val expected = listOf(listOf(a,b), listOf(c,d))
        val actual = listOf(a,b,c,d,e,f).findDuplicates { first, second -> first.sameAs(second) }
        assertEquals(expected, actual)
    }

    @Test
    fun testComparator(){
        val comparator: ImagesComparator = AndroidImagesComparator()

        val image1 = ImageInfo(PATH_1)
        val image2 = ImageInfo(PATH_2)

        assertFalse(comparator.invoke(image1, image2))
        assertTrue(comparator.invoke(image1, image1))
        assertTrue(comparator.invoke(image2, image2))
    }

    companion object{
        private const val PATH_1 = "src/test/res/pixel.png"
        private const val PATH_2 = "src/test/res/pixel_2.webp"
    }
}