package yin_kio.duplicates.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
                    isInProgress = it.isInProgress
                ))
            }
        }
    }

    fun isGroupSelected(position: Int) : Boolean{
        return state.isGroupSelected(position)
    }


}