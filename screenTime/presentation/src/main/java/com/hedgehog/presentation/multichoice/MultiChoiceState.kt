package com.hedgehog.presentation.multichoice

interface MultiChoiceState<T> {
    fun isChecked(item: T): Boolean
    val totalCheckedCount: Int
}