package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.device_battery_info.BatteryInfoImpl
import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DeviceInfoBindModule {

    @Binds
    fun bindBatteryInfoImplToBatteryInfo(batteryDeviceInfo: BatteryInfoImpl): BatteryInfo

}