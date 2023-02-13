package yin_kio.acceleration.domain.bg_uploading

interface AppsForm {

    val hasSelected: Boolean
    val isAllSelected: Boolean


    fun switchSelectAll()

    fun switchSelectApp(packageName: String)
    fun isAppSelected(packageName: String) : Boolean

}