package yin_kio.duplicates.domain.gateways

import yin_kio.duplicates.domain.models.ImageInfo

interface Files {
    suspend fun getImages() : List<ImageInfo>
    suspend fun delete(path: String)
    suspend fun copy(path: String, destination: String)
    fun unitedDestination() : String
}