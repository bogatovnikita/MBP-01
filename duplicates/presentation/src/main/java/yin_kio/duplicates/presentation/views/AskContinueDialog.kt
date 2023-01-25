package yin_kio.duplicates.presentation.views

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.databinding.DialogAskContinueBinding
import yin_kio.duplicates.presentation.view_models.getParentViewModel

class AskContinueDialog : DialogFragment(R.layout.dialog_ask_continue) {

    private val binding: DialogAskContinueBinding by viewBinding()
    private val viewModel by lazy { getParentViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, general.R.style.MyDialog)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.continueUnite.setOnClickListener { viewModel.continueUniting() }
        binding.complete.setOnClickListener { viewModel.completeUniting() }
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.updateFiles()
    }

}