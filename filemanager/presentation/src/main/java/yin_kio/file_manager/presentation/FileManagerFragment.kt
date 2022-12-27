package yin_kio.file_manager.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recycler_adapter.recyclerAdapter
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_manager.presentation.databinding.FragmentFileManagerBinding
import yin_kio.file_manager.presentation.databinding.ListItemBinding
import yin_kio.file_manager.presentation.models.UiState

class FileManagerFragment(
) : Fragment(R.layout.fragment_file_manager) {

    private val binding: FragmentFileManagerBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }

    private val adapter by lazy { adapter() }

    private fun adapter() = recyclerAdapter<FileInfo, ListItemBinding>(
        onViewHolderCreated = { holder ->
            observeState {
                holder.currentItem?.let {
                    checkBox.isChecked = it.isSelected
                }
            }
        },
        onBind = { item, _ ->
            name.text = item.name
            checkBox.isChecked = item.isSelected
            root.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectFile(item.path)) }
            checkBox.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectFile(item.path)) }
        },
        areItemsTheSame = { old, new -> old.path == new.path },
        areContentsTheSame = { _, _ -> false }
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupListeners()
        setupObserver()
    }

    private fun setupView(){
        binding.recycler.adapter = adapter
    }

    private fun setupListeners(){
        binding.apply {
            allFiles.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.AllFiles)) }
            images.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Images)) }
            audio.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Audio)) }
            video.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Video)) }
            documents.setOnClickListener { viewModel.obtainIntention(Intention.SwitchFileMode(FileRequest.Documents)) }

            selectAll.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectAll) }
            isAllSelected.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectAll) }
        }
    }

    private fun setupObserver() {
        observeState {  Log.d("FileManager", "isAllSelected: ${it.isAllSelected}")
            adapter.submitList(it.files)

            askPermission(it)
            showFileRequest(it)
            showIsAllSelected(it)
            showRecycler(it)
            showSortIcon(it)
            showDeleteButton(it)
        }
    }

    private fun observeState(action: (UiState) -> Unit){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect(action)
        }
    }

    private fun askPermission(it: UiState) {
        if (!it.hasPermission) {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.fileManagerFragment) {
                navController.navigate(R.id.permissionFragment)
            }
        }
    }

    private fun showRecycler(it: UiState) {
        if (binding.recycler.layoutManager!!::class != it.layoutManager::class){
            binding.recycler.layoutManager = it.layoutManager
        }
    }

    private fun showIsAllSelected(it: UiState) {
        binding.isAllSelected.isChecked = it.isAllSelected
    }

    private fun showSortIcon(it: UiState) {
        binding.sort.imageTintList = ColorStateList.valueOf(it.sortingIconColor)
    }

    private fun showDeleteButton(it: UiState) {
        binding.delete.background = ContextCompat.getDrawable(requireContext(), it.deleteButtonBg)
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