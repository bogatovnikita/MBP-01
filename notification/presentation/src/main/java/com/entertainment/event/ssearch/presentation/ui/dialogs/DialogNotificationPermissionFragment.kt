package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDialogNotificationPermissionBinding

class DialogNotificationPermissionFragment :
    DialogFragment() {

    private var binding: FragmentDialogNotificationPermissionBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentDialogNotificationPermissionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        setStyle(STYLE_NO_TITLE, R.style.WideDialogStyle)
        builder.setView(binding!!.root)
        setListeners()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun setListeners() {
        binding!!.btnAllowNotifications.setOnClickListener {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            dialog!!.cancel()
        }
        binding!!.btnClose.setOnClickListener {
            dialog!!.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}