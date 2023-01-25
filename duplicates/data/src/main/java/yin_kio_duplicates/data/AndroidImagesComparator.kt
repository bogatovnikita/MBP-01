package yin_kio_duplicates.data

import android.graphics.BitmapFactory
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.models.ImageInfo
import java.io.File

class AndroidImagesComparator : ImagesComparator {

    override fun invoke(p1: ImageInfo, p2: ImageInfo): Boolean {
        val first = File(p1.path)
        val second = File(p2.path)

        if (first.name == second.name){
            return isContentEquals(first, second)
        }
        return false
    }

    private fun isContentEquals(first: File, second: File): Boolean {
        val firstBitmap = BitmapFactory.decodeFile(first.absolutePath)
        val secondBitmap = BitmapFactory.decodeFile(second.absolutePath)
        return firstBitmap.sameAs(secondBitmap)
    }
}