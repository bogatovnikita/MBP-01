package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDialogNotificationPermissionBinding

class DialogNotificationPermissionFragment : DialogFragment(R.layout.fragment_dialog_notification_permission) {

    private val binding: FragmentDialogNotificationPermissionBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        setStyle(STYLE_NO_TITLE, R.style.WideDialogStyle)
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}