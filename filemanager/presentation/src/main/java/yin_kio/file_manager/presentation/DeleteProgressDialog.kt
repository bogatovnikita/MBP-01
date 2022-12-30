package yin_kio.file_manager.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.models.DeleteState
import yin_kio.file_manager.presentation.databinding.DialogDeleteProgressBinding

class DeleteProgressDialog : DialogFragment(R.layout.dialog_delete_progress) {

    private val binding: DialogDeleteProgressBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = noCancelableDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect {
                if (it.deleteState == DeleteState.Done) {
                    findNavController().apply {
                        navigateUp()
                        navigate(R.id.action_fileManagerFragment_to_doneDialog)
                    }
                }
                binding.title.text = it.deleteProgressTitle
            }
        }
    }


    private fun noCancelableDialog() = object : Dialog(requireContext()) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        override fun onBackPressed() {}
    }

}