package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.models.NotificationStateEvent
import com.entertainment.event.ssearch.presentation.ui.notification_manager.NotificationSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class DialogClearingFragment :
    DialogFragment(R.layout.fragment_dialog_clearing) {

    private val viewModel: NotificationSettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.WideDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        performingCleanup()
    }

    private fun performingCleanup() {
        lifecycleScope.launchWhenResumed {
            delay(8000)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.obtainEvent(NotificationStateEvent.CloseDialogClearing)
    }
}