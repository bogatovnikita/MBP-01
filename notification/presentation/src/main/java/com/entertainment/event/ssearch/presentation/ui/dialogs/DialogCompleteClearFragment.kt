package com.entertainment.event.ssearch.presentation.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDialogCompleteClearBinding


class DialogCompleteClearFragment :
    DialogFragment() {

    private var binding: FragmentDialogCompleteClearBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentDialogCompleteClearBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        setStyle(STYLE_NO_TITLE, R.style.WideDialogStyle)
        builder.setView(binding!!.root)
        setListeners()
        Log.e("!!!", "onCreateDialog")

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    private fun setListeners() {
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("!!!", "onDestroy")
        binding = null
    }
}