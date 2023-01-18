package yin_kio.duplicates.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder
import yin_kio.duplicates.presentation.models.Image

class ItemViewModel(
    private val switchItemSelection: (groupIndex: Int, path: String) -> Unit,
    private val stateHolder: StateHolder,
    private val coroutineScope: CoroutineScope,
) {



    val state = MutableStateFlow(Image())

    init {
        coroutineScope.launch {
            stateHolder.stateFlow.collect{
                val isSelected = it.isItemSelected(state.value.groupId, state.value.path)
                state.value = state.value.copy(isSelected = isSelected)
            }
        }
    }

    fun switchSelection(groupIndex: Int, path: String){
        switchItemSelection(groupIndex, path)
    }

    fun updateState(groupIndex: Int, path: String){
        state.value = state.value.copy(
            path = path,
            isSelected = stateHolder.isItemSelected(groupIndex, path),
            groupId = groupIndex
        )
    }

}