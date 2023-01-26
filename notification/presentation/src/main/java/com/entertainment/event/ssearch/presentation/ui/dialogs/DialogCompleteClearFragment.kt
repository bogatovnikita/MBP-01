package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDialogCompleteClearBinding


class DialogCompleteClearFragment :
    DialogFragment(R.layout.fragment_dialog_complete_clear) {

    private val binding: FragmentDialogCompleteClearBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.NormalDialogStyle)
    }

    override fun onResume() {
        super.onResume()
        initListeners()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initListeners() {
        binding.btnDone.setOnClickListener {
            dismiss()
        }
    }
}