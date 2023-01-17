package yin_kio.duplicates.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder
import yin_kio.duplicates.domain.use_cases.DuplicateUseCase
import kotlin.coroutines.CoroutineContext

class DuplicatesViewModel(
    useCase: DuplicateUseCase,
    private val state: StateHolder,
    coroutineScope: CoroutineScope,
    coroutineDispatcher: CoroutineContext
) : DuplicateUseCase by useCase{

    private val _uiState = MutableSharedFlow<UIState>()
    val uiState = _uiState.asSharedFlow()

    init {
        coroutineScope.launch(coroutineDispatcher) {
            state.stateFlow.collect{
                _uiState.emit(UIState(
                    destination = it.destination,
                    duplicatesList = it.duplicatesList,
                    isInProgress = it.isInProgress,
                    selected = it.selected,
                    buttonState = ButtonState(
                        bgResId = bgResId(it),
                        titleResId = titleResId(it)
                    )
                ))
            }
        }
    }

    private fun titleResId(it: StateHolder) =
        if (it.selected.isEmpty()) R.string.unite_all_duplicates else R.string.unite_selected_duplicates

    private fun bgResId(it: StateHolder) =
        if (it.selected.isEmpty()) general.R.drawable.bg_main_button_enabled else R.drawable.bg_unite_selected

    fun isGroupSelected(position: Int) : Boolean{
        return state.isGroupSelected(position)
    }

    fun isItemSelected(index: Int, path: String) : Boolean{
        return state.isItemSelected(index, path)
    }

}