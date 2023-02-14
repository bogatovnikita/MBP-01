package yin_kio.acceleration.domain.gateways

interface Apps {

    fun provide() : List<String>
    fun stop(apps: Collection<String>)

}