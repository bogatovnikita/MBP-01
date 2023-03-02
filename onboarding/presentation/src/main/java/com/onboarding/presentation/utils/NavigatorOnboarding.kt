package com.onboarding.presentation.utils

import com.onboarding.presentation.R

object NavigatorOnboarding {

    private val navigatorList: MutableList<Int> = mutableListOf()

    init {
        navigatorList.apply {
            add(R.id.toFirstStepFragment)
        }
    }

    fun getNavigationId(id: Int): Int {
        return navigatorList[id]
    }

}