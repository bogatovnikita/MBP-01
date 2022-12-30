package com.hedgehog.presentation.di

import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.multichoice.MultiChoiceHandler
import com.hedgehog.presentation.multichoice.SimpleMultiChoiceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MultiChoice

@Module
@InstallIn(ViewModelComponent::class)
class MultiChoiceModule {
    @Provides
    @MultiChoice
    fun provideMultiChoiceHandler(): MultiChoiceHandler<AppScreenTime> {
        return SimpleMultiChoiceHandler()
    }
}