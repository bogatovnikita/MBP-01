package yin_kio.file_manager.presentation.views

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.models.DeleteState
import yin_kio.file_manager.presentation.Intention
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.databinding.DialogAskDeleteBinding
import yin_kio.file_manager.presentation.navigation.Navigation
import yin_kio.file_manager.presentation.parentViewModel

class AskDeleteDialog : DialogFragment(R.layout.dialog_ask_delete) {

    private val binding: DialogAskDeleteBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }
    private lateinit var navigation: Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation = Navigation(findNavController())
        binding.apply {
            close.setOnClickListener { cancelDelete() }
            cancel.setOnClickListener { cancelDelete() }
            delete.setOnClickListener {
                viewModel.obtainIntention(Intention.Delete)
                navigation.goToDeleteProgress()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect {
                if (it.deleteState != DeleteState.Ask){
                    findNavController().navigateUp()
                }

                binding.title.text = it.askDeleteTitle
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        cancelDelete()
    }

    private fun cancelDelete() {
        viewModel.obtainIntention(Intention.CancelDelete)
    }

}