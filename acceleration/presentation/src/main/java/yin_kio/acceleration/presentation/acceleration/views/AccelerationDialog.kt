package yin_kio.acceleration.presentation.acceleration.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import yin_kio.acceleration.presentation.R

class AccelerationDialog : DialogFragment(R.layout.dialog_accelerate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayoutParams()
    }

    private fun setupLayoutParams() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return noCancelableDialog()
    }

    private fun noCancelableDialog() = object : Dialog(requireContext()) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
        }

        @Deprecated("Deprecated in Java")
        override fun onBackPressed() {}
    }


}