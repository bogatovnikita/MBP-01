package yin_kio.duplicates.domain.models

import kotlinx.coroutines.flow.Flow

interface State {
    val isInProgress: Boolean
    val duplicatesList: List<List<ImageInfo>>
    val selected: Set<ImageInfo>

    val stateFlow: Flow<State>
}