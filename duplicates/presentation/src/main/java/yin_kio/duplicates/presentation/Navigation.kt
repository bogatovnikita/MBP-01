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
        }
    }

    private fun Destination.adapt() : Int{
        return when(this){
            Destination.Permission -> TODO()
            Destination.List -> R.id.duplicatesFragment
            Destination.UniteProgress -> TODO()
            Destination.Inter -> TODO()
            Destination.DoneSelected -> TODO()
            Destination.DoneAll -> TODO()
        }
    }

    fun navigateUp(){
        navController.navigateUp()
    }

    private fun destinationIs(id: Int) : Boolean{
        return  navController.currentDestination?.id == id
    }

}