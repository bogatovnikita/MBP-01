package yin_kio.duplicates.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.duplicates.presentation.databinding.FragmentDuplicatesBinding

class DuplicatesFragment : Fragment(R.layout.fragment_duplicates) {

    private val binding: FragmentDuplicatesBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }

    private val adapter by lazy { DuplicatesAdapter(
        onImageClick = { groupIndex, item ->
            viewModel.switchItemSelection(groupIndex, item)
        },
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        stateFlow = viewModel.uiState
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect{
                showProgress(it)
                adapter.submitList(it.duplicatesList)
            }
        }
    }

    private fun showProgress(it: UIState) {
        binding.progressPanel.isVisible = it.isInProgress
        binding.listPanel.isVisible = !it.isInProgress
    }

}