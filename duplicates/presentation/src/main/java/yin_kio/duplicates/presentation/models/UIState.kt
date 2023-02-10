package yin_kio.duplicates.presentation.models

import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.domain.models.ImageInfo

data class UIState (
    val isClosed: Boolean = false,
    val destination: Destination = Destination.List,
    val duplicatesLists: List<DuplicatesList> = listOf(),
    val isInProgress: Boolean = true,
    val selected: Map<Int, Set<ImageInfo>> = mapOf(),
    val buttonState: ButtonState = ButtonState()
)

