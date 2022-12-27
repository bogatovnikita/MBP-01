package yin_kio.file_manager.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import yin_kio.file_manager.data.DataFactory
import yin_kio.file_manager.domain.FileManagerCreator
import yin_kio.file_manager.presentation.*

class FileManagerFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            FileManagerParentFragment::class.java.name -> FileManagerParentFragment(viewModelCreator())
            else -> super.instantiate(classLoader, className)
        }
    }

    private fun viewModelCreator(): ViewModel.(Context) -> FileManagerViewModel =
        { context ->
            FileManagerViewModel(
                fileManager = FileManagerCreator.create(
                    permissionChecker = DataFactory.createPermissionChecker(context),
                    files = DataFactory.createFiles(),
                    coroutineScope = viewModelScope
                ),
                presenter = Presenter(context)
            )
        }
}