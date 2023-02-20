package yin_kio.acceleration.presentation.inter

import android.app.Activity
import android.util.Log
import com.example.ads.showInter

class OlejaInter(
    var activity: Activity? = null,
    private val onClose: () -> Unit
) : Inter {



    override fun show() {
        activity?.showInter( onClosed = onClose )
    }
}