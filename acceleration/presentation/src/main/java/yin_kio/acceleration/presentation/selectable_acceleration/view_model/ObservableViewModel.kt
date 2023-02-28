package yin_kio.acceleration.presentation.selectable_acceleration.view_model

import kotlinx.coroutines.flow.Flow

interface ObservableViewModel {

    val flow: Flow<ScreenState>
    val commandsFlow: Flow<Any>

}