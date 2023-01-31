package yin_kio.garbage_clean.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanParentFragment : Fragment(R.layout.fragment_parent) {

    internal val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{
                handlePermission(it.hasPermission)
            }
        }

    }

    private fun handlePermission(
        hasPermission: Boolean
    ) {
        if (!hasPermission) {
            navController.navigate(R.id.permissionFragment)
        } else if (navController.destinationIs(R.id.permissionFragment)) {
            navController.navigateUp()
        }
    }

    private fun NavController.destinationIs(id: Int) : Boolean{
        return currentDestination?.id == id
    }


    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )
}