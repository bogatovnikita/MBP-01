package yin_kio.acceleration.domain.acceleration

sealed interface AppsState{

    object Progress: AppsState
    object Permission: AppsState
    data class AppsList(val apps: List<String>) : AppsState

}