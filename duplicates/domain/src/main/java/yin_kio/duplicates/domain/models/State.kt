package yin_kio.duplicates.domain.models

data class State(
    val isInProgress: Boolean = true,
    val duplicatesList: DuplicatesList = DuplicatesList()
)