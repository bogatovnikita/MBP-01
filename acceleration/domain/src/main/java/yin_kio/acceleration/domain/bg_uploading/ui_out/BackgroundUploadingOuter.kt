package yin_kio.acceleration.domain.bg_uploading.ui_out

import yin_kio.acceleration.domain.bg_uploading.entities.SelectionStatus

interface BackgroundUploadingOuter {

    fun close()

    fun updateApps()

    fun setAppSelected(packageName: String, isSelected: Boolean)

    fun setSelectionStatus(selectionStatus: SelectionStatus)

    fun setUpdateStatus(updateStatus: UpdateStatus)

    fun setApps(apps: List<String>)

    fun showStopProgress()

    fun showInter()

    fun complete()

}
