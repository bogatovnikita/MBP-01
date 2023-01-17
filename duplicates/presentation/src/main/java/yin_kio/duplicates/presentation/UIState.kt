package yin_kio.duplicates.presentation

import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo

data class UIState (
    val destination: Destination = Destination.List,
    val duplicatesList: List<List<ImageInfo>> = listOf(),
    val isInProgress: Boolean = true,
    val selected: Map<Int, Set<ImageInfo>> = mapOf()
)