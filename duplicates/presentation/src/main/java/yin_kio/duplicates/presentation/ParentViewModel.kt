package yin_kio.duplicates.presentation

import androidx.fragment.app.Fragment

fun Fragment.parentViewModel() = (parentFragment?.parentFragment as ParentFragment).viewModel