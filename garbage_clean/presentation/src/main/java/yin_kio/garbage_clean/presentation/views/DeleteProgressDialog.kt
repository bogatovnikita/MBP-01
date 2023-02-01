package yin_kio.garbage_clean.presentation.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import yin_kio.garbage_clean.presentation.R

class DeleteProgressDialog : DialogFragment(R.layout.dialog_delete_progress) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = noCancelableDialog()

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

        override fun onBackPressed() {}
    }

}