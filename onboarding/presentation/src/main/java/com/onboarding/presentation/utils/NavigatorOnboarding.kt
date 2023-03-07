package com.onboarding.presentation.utils

import com.onboarding.presentation.R

object NavigatorOnboarding {

    private val navigatorList: MutableList<Int> = mutableListOf()

    init {
        navigatorList.apply {
            add(R.id.toFirstStepFragment)
            add(R.id.toAboutDeviceFragment)
            add(R.id.toFullReadyFragment)
        }
    }

    fun getNavigationId(id: Int): Int {
        return navigatorList[id]
    }

}