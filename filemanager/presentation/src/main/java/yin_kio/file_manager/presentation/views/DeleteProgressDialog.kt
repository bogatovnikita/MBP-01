package yin_kio.file_manager.presentation.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.databinding.DialogFileManagerDeleteProgressBinding
import yin_kio.file_manager.presentation.parentViewModel

class DeleteProgressDialog : DialogFragment(R.layout.dialog_file_manager_delete_progress) {

    private val binding: DialogFileManagerDeleteProgressBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }




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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect {
                binding.title.text = it.deleteProgressTitle
            }
        }
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