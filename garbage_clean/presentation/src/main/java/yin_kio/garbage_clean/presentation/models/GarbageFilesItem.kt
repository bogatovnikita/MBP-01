package yin_kio.garbage_clean.presentation.models

import android.graphics.drawable.Drawable

data class GarbageFilesItem(
    val icon: Drawable,
    val name: String,
    val size: String,
    val isInProgress: Boolean
)
