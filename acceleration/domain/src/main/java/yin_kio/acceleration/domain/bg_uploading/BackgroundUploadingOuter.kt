package yin_kio.acceleration.domain.bg_uploading

interface BackgroundUploadingOuter {

    fun close()

    fun updateApps()
    fun setButtonEnabled(isEnabled: Boolean)
    fun setAppSelected(packageName: String, isSelected: Boolean)
    fun setAllSelected(isAllSelected: Boolean)

}
