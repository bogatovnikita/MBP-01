package yin_kio.duplicates.domain.models

import kotlinx.coroutines.flow.Flow

interface StateHolder {
    val isInProgress: Boolean
    val duplicatesList: List<List<ImageInfo>>
    val selected: Map<Int, Set<ImageInfo>>
    val destination: Destination


    fun isItemSelected(groupIndex: Int, path: String) : Boolean
    fun isGroupSelected(groupIndex: Int) : Boolean

    val stateFlow: Flow<StateHolder>
}