package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.device_battery_info.BatteryInfoImpl
import com.entertainment.event.ssearch.data.general_device_info.GeneralDeviceInfoImpl
import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DeviceInfoBindModule {

    @Binds
    fun bindBatteryInfoImplToBatteryInfo(batteryDeviceInfo: BatteryInfoImpl): BatteryInfo

    @Binds
    fun bindGeneralDeviceInfoImplToGeneralDeviceInfo(generalDeviceInfo: GeneralDeviceInfoImpl): GeneralDeviceInfo

}