package yin_kio.garbage_clean.presentation

import androidx.fragment.app.Fragment

fun Fragment.parentViewModel() = (parentFragment?.parentFragment as GarbageCleanParentFragment).viewModel