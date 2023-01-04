package yin_kio.file_manager.presentation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import jamycake.lifecycle_aware.lifecycleAware

class FileManagerParentFragment(
    private val viewModelCreator: ViewModel.(Activity) -> FileManagerViewModel
) : Fragment(R.layout.fragment_file_manager_parent) {

    val viewModel by lifecycleAware { viewModelCreator(requireActivity()) }
}