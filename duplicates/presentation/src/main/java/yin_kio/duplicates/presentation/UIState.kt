package yin_kio.duplicates.presentation

import yin_kio.duplicates.domain.models.Destination

data class UIState(
    val destination: Destination = Destination.List
)