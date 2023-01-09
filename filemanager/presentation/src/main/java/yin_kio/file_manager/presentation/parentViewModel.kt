package yin_kio.file_manager.presentation

import androidx.fragment.app.Fragment
import yin_kio.file_manager.presentation.views.FileManagerParentFragment

fun Fragment.parentViewModel() = (parentFragment?.parentFragment as FileManagerParentFragment).viewModel