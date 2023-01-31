package yin_kio.garbage_clean.presentation

import kotlinx.coroutines.flow.StateFlow
import yin_kio.garbage_clean.presentation.models.ScreenState

interface ObservableScreenViewModel {
    val state: StateFlow<ScreenState>
}