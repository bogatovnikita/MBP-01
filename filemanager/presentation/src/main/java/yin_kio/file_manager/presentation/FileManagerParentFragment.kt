package yin_kio.file_manager.presentation

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import jamycake.lifecycle_aware.lifecycleAware

class FileManagerParentFragment(
    private val viewModelCreator: ViewModel.(Context) -> FileManagerViewModel
) : Fragment(R.layout.fragment_file_manager_parent) {

    val viewModel by lifecycleAware { viewModelCreator(requireContext()) }
}