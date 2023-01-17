package yin_kio_duplicates.data

import android.graphics.BitmapFactory
import yin_kio.duplicates.domain.gateways.ImagesComparator
import yin_kio.duplicates.domain.models.ImageInfo

class AndroidImagesComparator : ImagesComparator {

    override fun invoke(p1: ImageInfo, p2: ImageInfo): Boolean {
        val first = BitmapFactory.decodeFile(p1.path)
        val second = BitmapFactory.decodeFile(p2.path)
        return first.sameAs(second)
    }
}