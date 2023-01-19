package yin_kio_duplicates.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.Dispatchers
import yin_kio.duplicates.domain.DuplicatesDomainFactory
import yin_kio.duplicates.presentation.Navigation
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.view_models.DuplicatesViewModel
import yin_kio.duplicates.presentation.fragments.ParentFragment
import yin_kio.file_utils.FileUtilsImpl
import yin_kio_duplicates.data.AndroidImagesComparator
import yin_kio_duplicates.data.FilesImpl
import yin_kio_duplicates.data.OlejaAds
import yin_kio_duplicates.data.PermissionsImpl

class DuplicatesFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when (className) {
            ParentFragment::class.java.name -> {

                return ParentFragment(
                    viewModelCreator = {
                        val (useCase, stateHolder) =  DuplicatesDomainFactory.createUseCaseAndStateHolder(
                            coroutineScope = viewModelScope,
                            files = FilesImpl(FileUtilsImpl()),
                            imagesComparator = AndroidImagesComparator(),
                            permissions = PermissionsImpl(it),
                            ads = OlejaAds(it)
                        )

                        DuplicatesViewModel(
                            useCase = useCase,
                            state = stateHolder,
                            coroutineScope = viewModelScope,
                            coroutineDispatcher = Dispatchers.Default
                        )
                    },
                    createNavigation = {
                        val childNavController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
                        Navigation(
                            childNavController = childNavController,
                            activity = requireActivity(),
                            onCloseInter = { it.closeInter() }
                        )
                    }
                )
            }
        }

        return super.instantiate(classLoader, className)
    }
}