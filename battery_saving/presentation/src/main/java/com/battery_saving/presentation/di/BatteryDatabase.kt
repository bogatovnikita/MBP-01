package com.battery_saving.presentation.di

import android.app.Application
import com.battery_saving.presentation.room.BatteryChargeDAO
import com.battery_saving.presentation.room.BatteryChargeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BatteryDatabase {

    @Singleton
    @Provides
    fun provideBatteryChargeDatabase(context: Application): BatteryChargeDatabase =
        BatteryChargeDatabase.create(context)

    @Provides
    fun provideBatteryChargeDAO(db: BatteryChargeDatabase): BatteryChargeDAO =
        db.getBatteryChargeStatisticsDao()
}