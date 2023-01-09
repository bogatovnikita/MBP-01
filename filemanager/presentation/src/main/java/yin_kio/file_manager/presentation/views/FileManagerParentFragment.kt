package yin_kio.file_manager.presentation.views

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.file_manager.presentation.FileManagerViewModel
import yin_kio.file_manager.presentation.R

class FileManagerParentFragment(
    private val viewModelCreator: ViewModel.(Activity) -> FileManagerViewModel
) : Fragment(R.layout.fragment_file_manager_parent) {

    val viewModel by lifecycleAware { viewModelCreator(requireActivity()) }
}