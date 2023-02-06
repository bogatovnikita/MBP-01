package yin_kio.duplicates.presentation.views

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.duplicates.presentation.Navigation
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.models.UIState
import yin_kio.duplicates.presentation.view_models.DuplicatesViewModel

class DuplicatesParentFragment(
    viewModelCreator: ViewModel.(Context) -> DuplicatesViewModel
) : Fragment(R.layout.fragment_duplicates_parent) {

    internal val viewModel by lifecycleAware(creator = {viewModelCreator(requireActivity().applicationContext)})

    private lateinit var navigation: Navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation = createNavigation()
        setupObserver()
    }

    private fun createNavigation() : Navigation{
        val childNavController = (childFragmentManager.findFragmentById(R.id.duplicates_fragment_container) as NavHostFragment).navController
        return Navigation(
            childNavController = childNavController,
            activity = requireActivity(),
            onCloseInter = { viewModel.closeInter() },
            completeDestination = requireArguments().getInt("completeId"),
            parentNavController = findNavController()
        )
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                goBackIfClosed(it)
                navigation.navigate(it.destination)
            }
        }
    }

    private fun goBackIfClosed(it: UIState) {
        if (it.isClosed) {
            findNavController().navigateUp()
        }
    }

}