package yin_kio.applications.domain

interface Apps {
    fun provideSystem() : List<App>
    fun provideEstablished() : List<App>
}