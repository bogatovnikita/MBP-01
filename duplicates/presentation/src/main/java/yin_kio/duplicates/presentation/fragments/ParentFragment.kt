package yin_kio.duplicates.presentation.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.duplicates.presentation.Navigation
import yin_kio.duplicates.presentation.R
import yin_kio.duplicates.presentation.view_models.DuplicatesViewModel

class ParentFragment(
    viewModelCreator: ViewModel.(Activity) -> DuplicatesViewModel
) : Fragment(R.layout.fragment_parent) {

    internal val viewModel by lifecycleAware(creator = {viewModelCreator(requireActivity())})

    private lateinit var navigation: Navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val childNavController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        navigation = Navigation(
            childNavController = childNavController,
            activity = requireActivity(),
            onCloseInter = { viewModel.closeInter() }
        )
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect{
                navigation.navigate(it.destination)
            }
        }
    }

}