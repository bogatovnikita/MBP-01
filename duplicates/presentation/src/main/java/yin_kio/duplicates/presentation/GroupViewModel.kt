package yin_kio.duplicates.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder

class GroupViewModel(
    private val stateHolder: StateHolder,
    private val switchGroupSelection: (index: Int) -> Unit,
    private val switchItemSelection: (groupIndex: Int, path: String) -> Unit,
    private val coroutineScope: CoroutineScope,
) {

    val state = MutableStateFlow(false)
    private val itemViewModels: MutableList<ItemViewModel> = mutableListOf()

    private var groupPosition = -1

    init {
        coroutineScope.launch{
            stateHolder.stateFlow.collect{
                state.emit(it.isGroupSelected(groupPosition))
            }
        }
    }

    fun switchSelection(position: Int){
        switchGroupSelection(position)
        state.value = stateHolder.isGroupSelected(position)
    }

    fun updateState(position: Int){
        this.groupPosition = position
        state.value = stateHolder.isGroupSelected(position)
    }

    fun createItemViewModel() : ItemViewModel{
        val itemViewModel = ItemViewModel(
            switchItemSelection = switchItemSelection,
            stateHolder = stateHolder,
            coroutineScope = coroutineScope
        )
        itemViewModels.add(itemViewModel)
        return itemViewModel
    }
}