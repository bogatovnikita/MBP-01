package yin_kio.file_manager.presentation.views

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ads.showInter
import yin_kio.file_manager.domain.models.DeleteState
import yin_kio.file_manager.domain.models.FileRequest
import yin_kio.file_manager.presentation.Intention
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.databinding.FragmentFileManagerBinding
import yin_kio.file_manager.presentation.models.UiState
import yin_kio.file_manager.presentation.navigation.Navigation
import yin_kio.file_manager.presentation.parentViewModel
import yin_kio.file_manager.presentation.sortingPopup

class FileManagerFragment(
) : Fragment(R.layout.fragment_file_manager) {

    private val binding: FragmentFileManagerBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }

    private lateinit var navigation: Navigation



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation = Navigation(findNavController())

        setupListeners()

        setupObserversOnItems()
        setupObserverOnScreen()
    }

    private fun setupObserversOnItems(){
        binding.recycler.onViewHolderCreated = {holder, checkBox ->
            observeState { checkBox.isChecked = holder.currentItem?.isSelected?:false }
        }
    }

    private fun setupListeners(){
        binding.apply {
            allFiles.setOnClickListener { viewModel.obtainIntention(
                Intention.SwitchFileMode(
                    FileRequest.AllFiles
                )
            ) }
            images.setOnClickListener { viewModel.obtainIntention(
                Intention.SwitchFileMode(
                    FileRequest.Images
                )
            ) }
            audio.setOnClickListener { viewModel.obtainIntention(
                Intention.SwitchFileMode(
                    FileRequest.Audio
                )
            ) }
            video.setOnClickListener { viewModel.obtainIntention(
                Intention.SwitchFileMode(
                    FileRequest.Video
                )
            ) }
            documents.setOnClickListener { viewModel.obtainIntention(
                Intention.SwitchFileMode(
                    FileRequest.Documents
                )
            ) }

            selectAll.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectAll) }
            isAllSelected.setOnClickListener { viewModel.obtainIntention(Intention.SwitchSelectAll) }

            recycler.setOnItemClickListener { viewModel.obtainIntention(
                Intention.SwitchSelectFile(
                    it
                )
            ) }
            showingMode.setOnClickListener { viewModel.obtainIntention(Intention.SwitchShowingMode) }
            sort.setOnClickListener { viewModel.obtainIntention(Intention.ShowSortingModeSelector) }

            delete.setOnClickListener { viewModel.obtainIntention(Intention.AskDelete) }
        }
    }

    private fun setupObserverOnScreen() {
        observeState { state ->
            askPermission(state)
            showFileRequest(state)
            showIsAllSelected(state)
            showRecycler(state)
            showList(state)
            showSortIcon(state)
            showDeleteButton(state)
            showListShowingMode(state)
            showSortingPopup(state)
            showProgress(state)
            showAskDelete(state)
            showInter(state)
        }
    }

    private fun showAskDelete(state: UiState) {
        if (state.deleteState == DeleteState.Ask) navigation.askDelete()
    }

    private fun showInter(state: UiState) {
        if (state.isShowInter) {
            showInter { viewModel.obtainIntention(Intention.HideInter) }
        }
    }

    private fun showList(state: UiState) {
        binding.recycler.mutableAdapter?.submitList(state.files)
    }

    private fun showProgress(state: UiState){
        binding.apply {
            recycler.alpha = state.progressAlpha
            delete.alpha = state.progressAlpha
            progressPlate.isVisible = state.inProgress
        }
    }

    private fun showListShowingMode(state: UiState) {
        binding.showingMode.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                state.listShowingModeIconRes
            )
        )
    }

    private fun showSortingPopup(state: UiState) {
        if (state.isShowSortingModeSelector) {
            sortingPopup(state,
                onDismiss = { viewModel.obtainIntention(Intention.HideSortingModeSelector) },
                onItemCLick = { viewModel.obtainIntention(Intention.SwitchSortingMode(it)) }
            ).showAsDropDown(binding.sort)
        }
    }

    private fun observeState(action: (UiState) -> Unit){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect(action)
        }
    }

    private fun askPermission(it: UiState) {
        if (!it.hasPermission) navigation.askPermission()
    }


    private fun showRecycler(it: UiState) {
        binding.recycler.setListShowingMode(it.listShowingMode)
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