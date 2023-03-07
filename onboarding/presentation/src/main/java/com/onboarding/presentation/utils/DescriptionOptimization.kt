package com.onboarding.presentation.utils

import com.onboarding.presentation.R

object DescriptionOptimization {

    private val descriptionsList: MutableList<Int> = mutableListOf()

    init {
        descriptionsList.apply {
            add(R.string.do_not_close_or_minimize_the_application)
            add(R.string.do_not_close_or_minimize_the_application_while_it_is_being_checked)
            add(R.string.do_not_close_or_minimize_the_application_while_going_optimized)
        }
    }

    fun getDescriptionForOptimization(id: Int): Int {
        return descriptionsList[id]
    }
}