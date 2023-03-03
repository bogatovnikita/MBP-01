package com.onboarding.presentation.utils

import android.content.Context


class PreferencesProvider(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun checkFirstLaunch() = sharedPreferences.getBoolean(FIRST_LAUNCH, true)

    fun saveNotFirstLaunch() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, false).apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "PREFERENCES_NAME"
        private const val FIRST_LAUNCH = "FIRST_LAUNCH"
    }
}
