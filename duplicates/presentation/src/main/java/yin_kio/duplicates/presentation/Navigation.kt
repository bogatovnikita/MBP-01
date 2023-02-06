package yin_kio.duplicates.presentation

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.ads.showInter
import yin_kio.duplicates.domain.models.Destination

class Navigation(
    private val childNavController: NavController,
    private val parentNavController: NavController,
    private val activity: Activity,
    private val onCloseInter: () -> Unit,
    private val completeDestination: Int
) {

    private var currentDestination = Destination.List

    private  val navigateUpDestinations = arrayOf(
        Destination.List,
        Destination.AskContinue
    )

    private val bundle: Bundle = Bundle()

    fun navigate(destination: Destination){
        if (currentDestination == destination) return

        if (destination == Destination.Advices || destination == Destination.AdvicesWithDialog
        ) {
            parentNavController.navigate(completeDestination, bundle)
            return
        }

        if (navigateUpDestinations.contains(destination)) navigateUp()

        when(val id = destination.adapt()){
            INTER_ID -> activity.showInter(onClosed = onCloseInter)
            else -> childNavController.navigate(id)
        }


        currentDestination = destination
    }

    private fun Destination.adapt() : Int{
        return when(this){
            Destination.Permission -> R.id.action_duplicatesFragment_to_duplicatesPermissionFragment
            Destination.List -> R.id.duplicatesFragment
            Destination.UniteProgress -> R.id.action_duplicatesFragment_to_progressDialog
            Destination.Inter -> INTER_ID
            Destination.AskContinue -> R.id.action_duplicatesFragment_to_askContinueDialog
            Destination.Advices -> completeDestination
            Destination.AdvicesWithDialog -> completeDestination
        }
    }

    private fun navigateUp(){
        childNavController.navigateUp()
    }

    companion object{
        private const val INTER_ID = -1
    }

}