package yin_kio.duplicates.domain

import yin_kio.duplicates.domain.models.ImageInfo

interface Files {
    suspend fun getImages() : List<ImageInfo>
}