package yin_kio.file_manager.presentation.navigation

import androidx.navigation.NavController
import yin_kio.file_manager.presentation.R

class Navigation(
    private val navController: NavController
) {

    fun askDelete(){
        navigate(R.id.action_fileManagerFragment_to_askDeleteDialog)
    }

    fun askPermission(){
        navigate(R.id.toPermissionFragment)
    }

    fun goToDeleteProgress(){
        navController.navigateUp()
        navController.navigate(R.id.action_fileManagerFragment_to_deleteProgressDialog)
    }

    private fun navigate(id: Int){
        if (navController.currentDestination?.id == R.id.fileManagerFragment) {
            navController.navigate(id)
        }
    }

}