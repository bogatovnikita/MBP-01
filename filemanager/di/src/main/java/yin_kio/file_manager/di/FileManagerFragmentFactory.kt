package yin_kio.file_manager.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.viewModelScope
import yin_kio.file_manager.data.DataFactory
import yin_kio.file_manager.domain.FileManagerCreator
import yin_kio.file_manager.presentation.FileManagerFragment
import yin_kio.file_manager.presentation.FileManagerViewModel
import yin_kio.file_manager.presentation.Presenter

class FileManagerFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if (className == FileManagerFragment::class.java.name){

            return FileManagerFragment(
                viewModelCreator = { context ->
                    FileManagerViewModel(
                        fileManager = FileManagerCreator.create(
                            permissionChecker = DataFactory.createPermissionChecker(context),
                            files = DataFactory.createFiles(),
                            coroutineScope = viewModelScope
                        ),
                        presenter =  Presenter(context)
                    )
                }
            )

        }
        return super.instantiate(classLoader, className)
    }
}