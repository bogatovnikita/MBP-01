package yin_kio.garbage_clean.presentation.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ads.showInter
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanParentFragment : Fragment(R.layout.fragment_parent) {

    internal val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{
                showPermissionScreen(it.hasPermission)
                showDeleteProgress(it)
                showInter(it)
            }
        }

    }

    private fun showInter(screenState: ScreenState){
        if (screenState.deleteProgressState == DeleteProgressState.Complete
            && destinationIs(R.id.deleteProgressDialog)
        ){
            navController.navigateUp()
            showInter { Toast.makeText(requireContext(), "NotImplemented yet", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun showDeleteProgress(it: ScreenState) {
        if (it.deleteProgressState == DeleteProgressState.Progress
            && destinationIs(R.id.garbageCleanFragment)
        ) {
            navController.navigate(R.id.deleteProgressDialog)
        }
    }

    private fun showPermissionScreen(
        hasPermission: Boolean
    ) {
        if (!hasPermission) {
            navController.navigate(R.id.permissionFragment)
        } else if (destinationIs(R.id.permissionFragment)) {
            navController.navigateUp()
        }
    }

    private fun destinationIs(id: Int) : Boolean{
        return navController.currentDestination?.id == id
    }

    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )
}