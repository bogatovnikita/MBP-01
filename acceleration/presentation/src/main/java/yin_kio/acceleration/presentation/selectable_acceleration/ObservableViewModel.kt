package yin_kio.acceleration.presentation.selectable_acceleration

import kotlinx.coroutines.flow.Flow

interface ObservableViewModel {

    val flow: Flow<ScreenState>

}