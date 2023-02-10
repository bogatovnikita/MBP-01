package yin_kio.memory.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.storage_info_view.databinding.LayoutStorageInfoBinding
import jamycake.lifecycle_aware.lifecycleAware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.memory.data.RamInfoProvider
import yin_kio.memory.data.StorageInfoProvider
import yin_kio.memory.domain.MemoryUseCases
import yin_kio.memory.presentation.databinding.FragmentMemoryBinding

class MemoryFragment : Fragment(R.layout.fragment_memory) {

    private val binding: FragmentMemoryBinding by viewBinding()
    private val viewModel: MemoryViewModel by lifecycleAware { createMemoryViewModel(viewModelScope) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.back.setOnClickListener { findNavController().navigateUp() }
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                binding.ram.binding.showMemoryState(it.ram)
                binding.storage.binding.showMemoryState(it.storage)
            }
        }
    }

    private fun LayoutStorageInfoBinding.showMemoryState(memoryState: MemoryState) {
        progressBar.progress = memoryState.progress
        occupied.text = memoryState.occupied
        available.text = memoryState.available
        total.text = memoryState.total
    }

    private fun createMemoryViewModel(
        coroutineScope: CoroutineScope
    ) : MemoryViewModel{
        val context = requireActivity().applicationContext

        val outer = MemoryOuter(
            presenter = MemoryPresenterImpl(context)
        )
        val vm = MemoryViewModelImpl(
            useCases = MemoryUseCases(
                outBoundary = outer,
                ramInfo = RamInfoProvider(context),
                storageInfo = StorageInfoProvider(context),
                coroutineScope = coroutineScope,
                dispatcher = Dispatchers.IO
            )
        )
        outer.viewModel = vm

        return vm
    }


}