package com.entertainment.event.ssearch.presentation.di

import com.entertainment.event.ssearch.data.device_battery_info.BatteryInfoImpl
import com.entertainment.event.ssearch.data.device_functionality_info.FunctionalityInfoImpl
import com.entertainment.event.ssearch.data.device_processor_info.ProcessorInfoImpl
import com.entertainment.event.ssearch.data.general_device_info.GeneralDeviceInfoImpl
import com.entertainment.event.ssearch.domain.device_info.BatteryInfo
import com.entertainment.event.ssearch.domain.device_info.FunctionalityInfo
import com.entertainment.event.ssearch.domain.device_info.GeneralDeviceInfo
import com.entertainment.event.ssearch.domain.device_info.ProcessorInfo
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

    @Binds
    fun bindFunctionalityInfoImplToFunctionalityInfo(functionalityInfo: FunctionalityInfoImpl): FunctionalityInfo

    @Binds
    fun bindProcessorInfoImplToProcessorInfo(processorInfo: ProcessorInfoImpl): ProcessorInfo

}