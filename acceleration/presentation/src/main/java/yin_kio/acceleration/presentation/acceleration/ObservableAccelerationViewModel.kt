package yin_kio.acceleration.presentation.acceleration

import kotlinx.coroutines.flow.Flow

interface ObservableAccelerationViewModel {

    val flow: Flow<ScreenState>

}