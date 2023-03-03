package com.entertainment.event.ssearch.presentation.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefProviderModule {

    @Provides
    @Singleton
    fun getSharedPreferencesProvider(context: Application): SharedPreferences =
        context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

}