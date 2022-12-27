package yin_kio.file_manager.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recycler_adapter.recyclerAdapter
import jamycake.lifecycle_aware.lifecycleAware
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_manager.presentation.databinding.FragmentFileManagerBinding
import yin_kio.file_manager.presentation.models.UiState

class FileManagerFragment(
    viewModelCreator: ViewModel.(Context) -> FileManagerViewModel
) : Fragment(R.layout.fragment_file_manager) {

    private val binding: FragmentFileManagerBinding by viewBinding()
    private val viewModel by lifecycleAware{viewModelCreator(requireContext())}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupListeners()
        setupObserver()
    }

    private fun setupView(){
//        binding.recycler.adapter = recyclerAdapter()
    }

    private fun setupListeners(){
        binding.apply {
            allFiles.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.AllFiles)) }
            images.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Images)) }
            audio.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Audio)) }
            video.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Video)) }
            documents.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Documents)) }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect {
                showIsAllSelected(it)
                showRecycler(it)
                showSortIcon(it)
                showDeleteButton(it)
                showFileRequest(it)
            }
        }
    }

    private fun showRecycler(it: UiState) {
        binding.recycler.layoutManager = it.layoutManager
    }

    private fun showIsAllSelected(it: UiState) {
        binding.isAllSelected.isChecked = it.isAllSelected
    }

    private fun showSortIcon(it: UiState) {
        binding.sort.imageTintList = ColorStateList.valueOf(it.sortingIconColor)
    }

    private fun showDeleteButton(it: UiState) {
        binding.delete.backgroundTintList = ColorStateList.valueOf(it.deleteButtonColor)
    }

    private fun showFileRequest(it: UiState) {
        binding.apply {
            allFiles.isChecked = it.fileRequest == FileRequest.AllFiles
            images.isChecked = it.fileRequest == FileRequest.Images
            audio.isChecked = it.fileRequest == FileRequest.Audio
            video.isChecked = it.fileRequest == FileRequest.Video
            documents.isChecked = it.fileRequest == FileRequest.Documents
        }
    }

}