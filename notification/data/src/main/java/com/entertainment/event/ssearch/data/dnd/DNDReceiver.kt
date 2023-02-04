package com.entertainment.event.ssearch.data.dnd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DNDReceiver: BroadcastReceiver() {

    @Inject
    lateinit var dndControllerImpl: DNDControllerImpl

    override fun onReceive(context: Context, intent: Intent?) {
    }

}