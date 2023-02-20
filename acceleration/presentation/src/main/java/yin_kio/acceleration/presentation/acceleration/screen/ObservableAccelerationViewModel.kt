package yin_kio.acceleration.presentation.acceleration.screen

import kotlinx.coroutines.flow.Flow

interface ObservableAccelerationViewModel {

    val flow: Flow<ScreenState>

}