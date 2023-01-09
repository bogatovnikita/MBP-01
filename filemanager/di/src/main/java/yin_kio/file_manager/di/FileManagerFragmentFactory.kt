package yin_kio.file_manager.di

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import yin_kio.file_manager.data.DataFactory
import yin_kio.file_manager.data.OlejaAds
import yin_kio.file_manager.domain.FileManagerCreator
import yin_kio.file_manager.presentation.views.FileManagerParentFragment
import yin_kio.file_manager.presentation.FileManagerViewModel
import yin_kio.file_manager.presentation.presenters.FileManagerPresenter

class FileManagerFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            FileManagerParentFragment::class.java.name -> FileManagerParentFragment(viewModelCreator())
            else -> super.instantiate(classLoader, className)
        }
    }

    private fun viewModelCreator(): ViewModel.(Activity) -> FileManagerViewModel =
        { context ->
            FileManagerViewModel(
                fileManager = FileManagerCreator.create(
                    permissionChecker = DataFactory.createPermissionChecker(context),
                    files = DataFactory.createFiles(),
                    coroutineScope = viewModelScope,
                    ads = OlejaAds(context)
                ),
                presenter = FileManagerPresenter(context)
            )
        }
}