package com.onboarding.presentation.utils

import com.onboarding.presentation.R

object DescriptionOptimization {

    private val descriptionsList: MutableList<Int> = mutableListOf()

    init {
        descriptionsList.apply {
            add(R.string.do_not_close_or_minimize_the_application_while_threats_are_being_eliminated_we_need_a_couple_more_seconds)
        }
    }

    fun getDescriptionForOptimization(id: Int): Int {
        return descriptionsList[id]
    }
}