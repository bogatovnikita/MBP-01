package yin_kio.file_manager.presentation

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.file_manager.presentation.databinding.FragmentFileManagerBinding

class FileManagerFragment : Fragment(R.layout.fragment_file_manager) {

    private val binding: FragmentFileManagerBinding by viewBinding()
    private val viewModel by lifecycleAware{}


}