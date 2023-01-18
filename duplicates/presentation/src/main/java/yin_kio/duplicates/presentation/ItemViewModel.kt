package yin_kio.duplicates.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder

class ItemViewModel(
    private val switchItemSelection: (groupIndex: Int, path: String) -> Unit,
    private val stateHolder: StateHolder,
    private val coroutineScope: CoroutineScope,
) {



    val state = MutableSharedFlow<Boolean>()
    var groupIndex = -1
    var path = ""

    init {
        coroutineScope.launch {
            stateHolder.stateFlow.collect{
                state.emit(it.isItemSelected(groupIndex, path))
            }
        }
    }

    fun switchSelection(groupIndex: Int, path: String){
        switchItemSelection(groupIndex, path)
        coroutineScope.launch { state.emit(stateHolder.isItemSelected(groupIndex, path)) }
    }

    fun updateState(groupIndex: Int, path: String){
        this.groupIndex = groupIndex
        this.path = path
        coroutineScope.launch { state.emit(stateHolder.isItemSelected(groupIndex, path)) }
    }

}