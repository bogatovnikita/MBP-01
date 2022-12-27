package yin_kio.file_manager.presentation

import androidx.fragment.app.Fragment

fun Fragment.parentViewModel() = (parentFragment?.parentFragment as FileManagerParentFragment).viewModel