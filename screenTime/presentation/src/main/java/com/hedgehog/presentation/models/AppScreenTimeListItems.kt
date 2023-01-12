package com.hedgehog.presentation.models

import android.graphics.drawable.Drawable

data class AppScreenTimeListItems(
    val originAppScreenTime: AppScreenTime,
    val isChecked: Boolean,
    val isSelectedMode: Boolean
) {
    val name: String get() = originAppScreenTime.name
    val time: String get() = originAppScreenTime.time
    val icon: Drawable? get() = originAppScreenTime.icon
}