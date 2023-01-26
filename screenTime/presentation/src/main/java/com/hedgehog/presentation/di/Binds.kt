package com.hedgehog.presentation.di

import com.hedgehog.data.repository_implementation.AppInfoRepositoryImplementation
import com.hedgehog.data.repository_implementation.ScreenTimeDataRepositoryImplementation
import com.hedgehog.domain.repository.AppInfoRepository
import com.hedgehog.domain.repository.ScreenTimeDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Binds {
    @Binds
    fun bindsScreenTimeDataRepositoryImplementationToScreenTimeDataRepository(
        screenTimeDataRepositoryImplementation: ScreenTimeDataRepositoryImplementation
    ): ScreenTimeDataRepository

    @Binds
    fun bindsAppInfoRepositoryImplementationToAppInfoRepository(
        appInfoRepositoryImplementation: AppInfoRepositoryImplementation
    ): AppInfoRepository
}