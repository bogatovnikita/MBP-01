package yin_kio.acceleration.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.acceleration.domain.acceleration.gateways.Permissions
import yin_kio.acceleration.domain.acceleration.gateways.RamInfo
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCases
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCasesImpl
import yin_kio.acceleration.domain.acceleration.use_cases.AcceleratorImpl
import yin_kio.acceleration.domain.bg_uploading.entities.AppsFormImpl
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps

object AccelerationDomainFactory {

    fun createAccelerationUseCases(
        outer: AccelerationOuter,
        permissions: Permissions,
        apps: Apps,
        ramInfo: RamInfo,
        coroutineScope: CoroutineScope,
        ads: Ads
    ) : AccelerationUseCases{

        val appsForm = AppsFormImpl()

        val accelerator = AcceleratorImpl(
            accelerationOuter = outer,
            appsForm = appsForm,
            apps = apps,
            ads = ads
        )

        return AccelerationUseCasesImpl(
            accelerationOuter = outer,
            permissions = permissions,
            runner = accelerator,
            ramInfo = ramInfo,
            apps = apps,
            coroutineScope = coroutineScope,
            dispatcher = Dispatchers.IO
        )
    }
}