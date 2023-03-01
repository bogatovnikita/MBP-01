package yin_kio.applications.domain.gateways

import yin_kio.applications.domain.core.App

interface Apps {
    fun provideSystem() : List<App>
    fun provideEstablished() : List<App>
}