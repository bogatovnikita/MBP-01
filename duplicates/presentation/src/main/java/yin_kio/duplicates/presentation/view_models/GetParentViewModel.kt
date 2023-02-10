package yin_kio.duplicates.presentation.view_models

import androidx.fragment.app.Fragment
import yin_kio.duplicates.presentation.views.DuplicatesParentFragment

internal fun Fragment.getParentViewModel() = (parentFragment?.parentFragment as DuplicatesParentFragment).viewModel