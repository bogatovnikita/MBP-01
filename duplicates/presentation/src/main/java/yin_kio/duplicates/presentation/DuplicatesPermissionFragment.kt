package yin_kio.duplicates.presentation

import general.PermissionFragment

class DuplicatesPermissionFragment : PermissionFragment<DuplicatesViewModel>() {

    override fun provideViewModel(): DuplicatesViewModel {
        return parentViewModel()
    }

    override fun actionOnResume(viewModel: DuplicatesViewModel) {
        viewModel.updateFiles()
    }
}