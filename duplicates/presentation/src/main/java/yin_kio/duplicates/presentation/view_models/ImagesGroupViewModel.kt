package yin_kio.duplicates.presentation.view_models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder
import yin_kio.duplicates.presentation.adapters.DuplicatesState

class ImagesGroupViewModel(
    private val stateHolder: StateHolder,
    private val switchGroupSelection: (index: Int) -> Unit,
    private val switchItemSelection: (groupIndex: Int, path: String) -> Unit,
    private val coroutineScope: CoroutineScope,
) {

    val state = MutableStateFlow(DuplicatesState())
    private val itemViewModels: MutableList<ItemViewModel> = mutableListOf()

    private var groupPosition = -1

    init {
        coroutineScope.launch(Dispatchers.Default){
            stateHolder.stateFlow.collect{
                state.emit(
                    newState(it)
                )
            }
        }
    }



    fun switchSelection(position: Int){
        switchGroupSelection(position)
        state.value = state.value.copy(isSelected = stateHolder.isGroupSelected(position))
    }

    fun updateState(position: Int){
        this.groupPosition = position
        state.value = newState(stateHolder)
    }

    private fun newState(it: StateHolder) = DuplicatesState(
        isSelected = it.isGroupSelected(groupPosition),
        isShowRestrictionMessage = it.selected[groupPosition]?.size == 1
    )


    fun createItemViewModel() : ItemViewModel {
        val itemViewModel = ItemViewModel(
            switchItemSelection = switchItemSelection,
            stateHolder = stateHolder,
            coroutineScope = coroutineScope
        )
        itemViewModels.add(itemViewModel)
        return itemViewModel
    }
}