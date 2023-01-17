package yin_kio.duplicates.presentation

import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo

data class UIState (
    val destination: Destination = Destination.List,
    val duplicatesList: List<List<ImageInfo>> = listOf(),
    val isInProgress: Boolean = true,
    val selected: Map<Int, Set<ImageInfo>> = mapOf(),
    val buttonState: ButtonState = ButtonState()
)

data class ButtonState(
    val bgResId: Int = general.R.drawable.bg_main_button_enabled,
    val titleResId: Int = R.string.unite_all_duplicates
)