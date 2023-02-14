package yin_kio.acceleration.domain.bg_uploading

interface BackgroundUploadingOuter {

    fun close()

    fun updateApps()

    fun setAppSelected(packageName: String, isSelected: Boolean)

    fun setSelectionStatus(selectionStatus: SelectionStatus)

    fun setUpdateStatus(updateStatus: UpdateStatus)

    fun setApps(apps: List<String>)

}