package yin_kio.applications.domain.ui_out

import yin_kio.applications.domain.core.App

interface DeleteRequester {

    fun delete(apps: Collection<App>)

}