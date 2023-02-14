package yin_kio.acceleration.domain.acceleration.ui_out

sealed interface AppsState{

    object Progress: AppsState
    object Permission: AppsState
    data class AppsList(val apps: List<String>) : AppsState

}