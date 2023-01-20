package yin_kio.duplicates.presentation.views

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.adapters.DuplicatesAdapter
import yin_kio.duplicates.presentation.databinding.FragmentDuplicatesBinding
import yin_kio.duplicates.presentation.models.UIState
import yin_kio.duplicates.presentation.view_models.getParentViewModel

class DuplicatesFragment : Fragment(R.layout.fragment_duplicates) {

    private val binding: FragmentDuplicatesBinding by viewBinding()
    private val viewModel by lazy { getParentViewModel() }

    private val adapter by lazy { DuplicatesAdapter(
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        createGroupViewModel = {viewModel.createGroupViewModel()},
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        binding.unite.setOnClickListener { viewModel.unite() }

        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
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