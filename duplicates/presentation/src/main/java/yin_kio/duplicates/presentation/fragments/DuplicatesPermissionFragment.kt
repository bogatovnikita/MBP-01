package yin_kio.duplicates.presentation.fragments

import general.PermissionFragment
import yin_kio.duplicates.presentation.view_models.DuplicatesViewModel
import yin_kio.duplicates.presentation.view_models.getParentViewModel

class DuplicatesPermissionFragment : PermissionFragment<DuplicatesViewModel>() {

    override fun provideViewModel(): DuplicatesViewModel {
        return getParentViewModel()
    }

    override fun actionOnResume(viewModel: DuplicatesViewModel) {
        viewModel.updateFiles()
    }
}