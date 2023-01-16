package yin_kio.file_manager.presentation.views

import general.PermissionFragment
import yin_kio.file_manager.presentation.FileManagerViewModel
import yin_kio.file_manager.presentation.Intention
import yin_kio.file_manager.presentation.parentViewModel

class FileManagerPermissionFragment : PermissionFragment<FileManagerViewModel>() {


    override fun actionOnResume(viewModel: FileManagerViewModel) {
        viewModel.obtainIntention(Intention.UpdateFiles)
    }
    override fun provideViewModel() = parentViewModel()


}