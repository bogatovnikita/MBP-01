package yin_kio.acceleration.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.acceleration.domain.acceleration.gateways.Permissions
import yin_kio.acceleration.domain.acceleration.gateways.RamInfo
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationOuter
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCases
import yin_kio.acceleration.domain.acceleration.use_cases.AccelerationUseCasesImpl
import yin_kio.acceleration.domain.acceleration.use_cases.AcceleratorImpl
import yin_kio.acceleration.domain.gateways.Ads
import yin_kio.acceleration.domain.gateways.Apps
import yin_kio.acceleration.domain.selectable_acceleration.entities.AppsFormImpl
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationOuter
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCases
import yin_kio.acceleration.domain.selectable_acceleration.use_cases.SelectableAccelerationUseCasesImpl

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

    fun createSelectableAccelerationUseCases(
        outer: SelectableAccelerationOuter,
        apps: Apps,
        coroutineScope: CoroutineScope,
        ads: Ads,
    ) : SelectableAccelerationUseCases{

        val appsForm = AppsFormImpl()

        return SelectableAccelerationUseCasesImpl(
            outer = outer,
            apps = apps,
            appsForm = appsForm,
            coroutineScope = coroutineScope,
            ads = ads,
            dispatcher = Dispatchers.IO
        )
    }
}