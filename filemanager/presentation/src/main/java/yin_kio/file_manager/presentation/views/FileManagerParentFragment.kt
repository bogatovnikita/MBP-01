package yin_kio.file_manager.presentation.views

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.file_manager.domain.models.DeleteState
import yin_kio.file_manager.presentation.FileManagerViewModel
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.models.ScreenState
import yin_kio.file_manager.presentation.navigation.Navigation

class FileManagerParentFragment(
    private val viewModelCreator: ViewModel.(Activity) -> FileManagerViewModel
) : Fragment(R.layout.fragment_file_manager_parent) {

    val viewModel by lifecycleAware { viewModelCreator(requireActivity()) }

    private lateinit var navigation: Navigation
    private lateinit var childNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childNavController = findChildNavController()
        navigation = Navigation(childNavController)
        setupObserver()
    }

    private fun findChildNavController() =
        (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                handleDeleteState(it)
                handlePermission(it)
            }
        }
    }

    private fun handleDeleteState(it: ScreenState) {
        when{
            it.deleteState == DeleteState.Ask -> navigation.askDelete()
            it.deleteState == DeleteState.Progress && destinationIs(R.id.askDeleteDialog) -> navigation.goToDeleteProgress()
            it.deleteState == DeleteState.Done && destinationIs(R.id.deleteProgressDialog) -> navigation.goToDone()
            it.deleteState == DeleteState.Wait && destinationIs(R.id.doneDialog) -> navigation.goBack()
            it.deleteState == DeleteState.Wait && destinationIs(R.id.askDeleteDialog) -> navigation.goBack()
        }
    }

    private fun handlePermission(
        it: ScreenState,
    ) {
        if (it.hasPermission) {
            goBackIfOnPermissionFragment()
        } else {
            navigation.askPermission()
        }
    }

    private fun goBackIfOnPermissionFragment() {
        if (destinationIs(R.id.permissionFragment)) {
            navigation.goBack()
        }
    }



    private fun destinationIs(id: Int) : Boolean{
        return  childNavController.currentDestination?.id == id
    }

}