package yin_kio.duplicates.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import jamycake.lifecycle_aware.lifecycleAware

class ParentFragment(
    viewModelCreator: ViewModel.(Context) -> DuplicatesViewModel
) : Fragment(R.layout.fragment_parent) {

    internal val viewModel by lifecycleAware(creator = {viewModelCreator(requireContext())})

    private lateinit var navigation: Navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        navigation = Navigation(navController)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect{
                navigation.navigate(it.destination)
            }
        }
    }

}