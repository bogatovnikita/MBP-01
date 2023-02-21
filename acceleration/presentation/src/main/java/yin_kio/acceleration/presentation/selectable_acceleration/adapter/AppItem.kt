package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.graphics.drawable.Drawable

data class AppItem(
    val packageName: String = "",
    val icon: Drawable? = null,
    val name: String = "",
    val isSelected: Boolean = false
)