package yin_kio.acceleration.presentation.acceleration.view_model

import kotlinx.coroutines.flow.Flow

interface ObservableAccelerationViewModel {

    val flow: Flow<ScreenState>

}