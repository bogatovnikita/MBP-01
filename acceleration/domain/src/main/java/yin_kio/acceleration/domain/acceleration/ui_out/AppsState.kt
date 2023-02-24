package yin_kio.acceleration.domain.acceleration.ui_out

import yin_kio.acceleration.domain.selectable_acceleration.entities.App

sealed interface AppsState{

    object Progress: AppsState
    object Permission: AppsState
    data class AppsList(val apps: List<App>) : AppsState

}