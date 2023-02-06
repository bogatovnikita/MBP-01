package yin_kio.garbage_clean.presentation.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ads.showInter
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanParentFragment : Fragment(R.layout.fragment_parent) {

    internal val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }
    private lateinit var childNavController: NavController

    private var completeId: Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        completeId = requireArguments().getInt("completeId")
        childNavController = findChildNavController()
        setupObserver()

    }

    private fun findChildNavController() =
        (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                goBackIfClosed(it)
                showPermissionScreen(it.hasPermission)
                showDeleteProgress(it)
                showInter(it)
            }
        }
    }



    private fun goBackIfClosed(it: ScreenState) {
        if (it.isClosed) {
            parentNavController()?.navigateUp()
        }
    }

    private fun showPermissionScreen(
        hasPermission: Boolean
    ) {
        if (!hasPermission) {
            childNavController.navigate(R.id.permissionFragment)
        } else if (destinationIs(R.id.permissionFragment)) {
            childNavController.navigateUp()
        }
    }

    private fun showDeleteProgress(it: ScreenState) {
        if (it.deleteProgressState == DeleteProgressState.Progress
            && destinationIs(R.id.garbageCleanFragment)
        ) {
            childNavController.navigate(R.id.deleteProgressDialog)
        }
    }

    private fun showInter(screenState: ScreenState){
        if (screenState.deleteProgressState == DeleteProgressState.Complete
            && destinationIs(R.id.deleteProgressDialog)
        ){
            childNavController.navigateUp()
            showInter {
                parentNavController()?.navigate(completeId)
            }
        }
    }

    private fun destinationIs(id: Int) : Boolean{
        return childNavController.currentDestination?.id == id
    }

    private fun parentNavController() = try {
        findNavController()
    } catch (ex: Exception) {
        null
    }






    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )
}