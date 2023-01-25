package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDialogClearingBinding
import com.entertainment.event.ssearch.presentation.models.NotificationStateEvent
import com.entertainment.event.ssearch.presentation.ui.notification_manager.NotificationSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class DialogClearingFragment :
    DialogFragment() {

    private var binding: FragmentDialogClearingBinding? = null

    private val viewModel: NotificationSettingsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentDialogClearingBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        setStyle(STYLE_NO_TITLE, R.style.WideDialogStyle)
        builder.setView(binding!!.root)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(false)
        dialog.apply { isCancelable = false }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        performingCleanup()
    }

    private fun performingCleanup() {
        lifecycleScope.launchWhenResumed {
            delay(8000)
            dismiss()
            viewModel.obtainEvent(NotificationStateEvent.CloseDialogClearing)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}