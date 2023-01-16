package yin_kio.duplicates.presentation

import androidx.fragment.app.Fragment
import general.PermissionFragment

class DuplicatesPermissionFragment : PermissionFragment<DuplicatesViewModel>() {

    override fun provideViewModel(): DuplicatesViewModel {
        return parentViewModel()
    }

    override fun actionOnResume(viewModel: DuplicatesViewModel) {
        viewModel.updateFiles()
    }

    fun Fragment.parentViewModel() = (parentFragment?.parentFragment as ParentFragment).viewModel
}