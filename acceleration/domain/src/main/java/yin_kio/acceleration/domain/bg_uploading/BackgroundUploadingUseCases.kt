package yin_kio.acceleration.domain.bg_uploading

interface BackgroundUploadingUseCases {
    fun close()

    fun switchSelectAllApps()
    fun switchSelectApp(packageName: String)
    fun update()
    fun stopSelectedApps()

    fun complete()
}