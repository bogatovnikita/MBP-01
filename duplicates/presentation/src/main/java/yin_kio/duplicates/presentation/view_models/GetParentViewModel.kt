package yin_kio.duplicates.presentation.view_models

import androidx.fragment.app.Fragment
import yin_kio.duplicates.presentation.fragments.ParentFragment

fun Fragment.getParentViewModel() = (parentFragment?.parentFragment as ParentFragment).viewModel