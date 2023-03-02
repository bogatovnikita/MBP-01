package yin_kio.applications.domain.ui_out

interface Navigator {

    fun close()
    fun showAskDeleteDialog()
    fun showDeleteProgressDialog()
    fun showInter()
    fun complete(freedSpace: Long)

}