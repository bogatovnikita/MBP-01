package yin_kio.acceleration.domain.gateways

interface Apps {

    suspend fun provide() : List<String>
    suspend fun stop(apps: Collection<String>)

}