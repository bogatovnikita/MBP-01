package yin_kio.duplicates.presentation

import androidx.navigation.NavController
import yin_kio.duplicates.domain.models.Destination

class Navigation(
    private val navController: NavController
) {

    private var currentDestination = Destination.List



    fun navigate(destination: Destination){
        if (currentDestination != destination){
            if (destination == Destination.List) navigateUp()
            val id = destination.adapt()
            if (id == INTER_ID) {
                // TODO showInter
            } else {
                navController.navigate(id)
            }

            currentDestination = destination
        }
    }

    private fun Destination.adapt() : Int{
        return when(this){
            Destination.Permission -> R.id.action_duplicatesFragment_to_duplicatesPermissionFragment
            Destination.List -> R.id.duplicatesFragment
            Destination.UniteProgress -> R.id.action_duplicatesFragment_to_progressDialog
            Destination.Inter -> INTER_ID
            Destination.DoneSelected -> TODO()
            Destination.DoneAll -> TODO()
        }
    }

    private fun navigateUp(){
        navController.navigateUp()
    }

    companion object{
        private const val INTER_ID = -1
    }

}