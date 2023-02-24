package yin_kio.acceleration.domain.gateways

import yin_kio.acceleration.domain.selectable_acceleration.entities.App

interface Apps {

    suspend fun provide() : List<App>
    suspend fun stop(apps: Collection<App>)

}