package yin_kio.duplicates.presentation.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import yin_kio.duplicates.presentation.R

class UniteProgressDialog : DialogFragment(R.layout.dialog_progress){


    override fun onCreateDialog(savedInstanceState: Bundle?) =  noCancelableDialog()

    override fun onStart() {
        super.onStart()
        setupLayoutParams()
    }

    private fun setupLayoutParams() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun noCancelableDialog() = object : Dialog(requireContext()) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
        }

        @Suppress("DEPRECATED")
        override fun onBackPressed() {}
    }

}