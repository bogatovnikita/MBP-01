package yin_kio.applications.domain

interface Navigator {

    fun close()
    fun showAskDeleteDialog()
    fun showDeleteProgressDialog()
    fun showInter()
    fun complete()

}