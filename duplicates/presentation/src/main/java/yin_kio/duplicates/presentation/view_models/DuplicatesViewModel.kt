package yin_kio.duplicates.presentation.view_models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.StateHolder
import yin_kio.duplicates.domain.use_cases.DuplicateUseCases
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.models.ButtonState
import yin_kio.duplicates.presentation.models.UIState
import kotlin.coroutines.CoroutineContext

class DuplicatesViewModel(
    useCase: DuplicateUseCases,
    private val state: StateHolder,
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineContext
) : DuplicateUseCases by useCase{

    private val _uiState = MutableSharedFlow<UIState>(
        replay = 1
    )
    val uiState = _uiState.asSharedFlow()

    private val imagesGroupViewModels = mutableListOf<ImagesGroupViewModel>()

    init { setupObserver() }

    private fun setupObserver() {
        coroutineScope.launch(coroutineDispatcher) {
            state.stateFlow.collect {
                _uiState.emit(presentState(it))
            }
        }
    }


    fun createImagesGroupViewModel() : ImagesGroupViewModel {
        val imagesGroupViewModel = ImagesGroupViewModel(
            stateHolder = state,
            switchGroupSelection = { switchGroupSelection(it) },
            switchItemSelection = ::switchItemSelection,
            coroutineScope = coroutineScope
        )
        imagesGroupViewModels.add(imagesGroupViewModel)
        return imagesGroupViewModel
    }



    private fun presentState(it: StateHolder) = UIState(
        isClosed = it.isClosed,
        destination = it.destination,
        duplicatesLists = it.duplicatesLists,
        isInProgress = it.isInProgress,
        selected = it.selected,
        buttonState = ButtonState(
            bgResId = presentBg(it),
            titleResId = presentTitle(it),
        )
    )

    private fun presentTitle(it: StateHolder) =
        if (it.selected.isEmpty()) R.string.unite_all_duplicates else R.string.unite_selected_duplicates

    private fun presentBg(it: StateHolder) : Int {
        return when{
            it.selected.isEmpty() -> general.R.drawable.bg_main_button_enabled
            !it.canUnite -> R.drawable.bg_unite_selected_disabled
            else -> R.drawable.bg_unite_selected
        }
    }


}