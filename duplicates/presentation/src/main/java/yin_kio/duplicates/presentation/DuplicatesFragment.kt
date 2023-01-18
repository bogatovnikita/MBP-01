package yin_kio.duplicates.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
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
        onGroupSelectClick = {viewModel.switchGroupSelection(it)},
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        stateFlow = viewModel.uiState,
        isGroupSelected = { viewModel.isGroupSelected(it) },
        isItemSelected = { groupIndex, path -> viewModel.isItemSelected(groupIndex, path) }
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect{
                showProgress(it)
                showButton(it)

                adapter.submitList(it.duplicatesLists)
            }
        }
    }

    private fun showButton(it: UIState) {
        binding.unite.setBackgroundResource(it.buttonState.bgResId)
        binding.unite.text = getString(it.buttonState.titleResId)
    }

    private fun showProgress(it: UIState) {
        binding.progressPanel.isInvisible = !it.isInProgress
        binding.listPanel.isInvisible = it.isInProgress
    }

}