package yin_kio.acceleration.domain.selectable_acceleration.ui_out

import yin_kio.acceleration.domain.selectable_acceleration.entities.App

interface AppsFormState {

    fun isAppSelected(app: App) : Boolean

}