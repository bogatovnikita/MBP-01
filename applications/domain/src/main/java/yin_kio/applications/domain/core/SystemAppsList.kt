package yin_kio.applications.domain.core

interface SystemAppsList {

    var content: List<App>
    var isVisible: Boolean

    fun sort()

}