package com.onboarding.presentation.utils

import com.onboarding.presentation.R

object DescriptionOptimization {

    private val descriptionsList: MutableList<Int> = mutableListOf()

    init {
        descriptionsList.apply {
            add(R.string.do_not_close_or_minimize_the_application)
        }
    }

    fun getDescriptionForOptimization(id: Int): Int {
        return descriptionsList[id]
    }
}