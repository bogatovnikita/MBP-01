package yin_kio.file_manager.presentation.views

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.file_manager.presentation.Intention
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.databinding.DialogDoneBinding
import yin_kio.file_manager.presentation.parentViewModel

class DoneDialog : DialogFragment(R.layout.dialog_done) {

    private val binding: DialogDoneBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ok.setOnClickListener { completeDelete() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect{
                binding.title.text = it.doneTitle
            }
        }
    }

    private fun completeDelete() {
        viewModel.obtainIntention(Intention.CompleteDelete)
    }

    override fun onDismiss(dialog: DialogInterface) {
        completeDelete()
    }

}