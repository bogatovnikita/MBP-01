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
            navController.navigate(destination.adapt())
            currentDestination = destination
        }
    }

    private fun Destination.adapt() : Int{
        return when(this){
            Destination.Permission -> R.id.action_duplicatesFragment_to_duplicatesPermissionFragment
            Destination.List -> R.id.duplicatesFragment
            Destination.UniteProgress -> TODO()
            Destination.Inter -> TODO()
            Destination.DoneSelected -> TODO()
            Destination.DoneAll -> TODO()
        }
    }

    private fun navigateUp(){
        navController.navigateUp()
    }

}