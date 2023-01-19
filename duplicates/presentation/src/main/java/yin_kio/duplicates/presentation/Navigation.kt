package yin_kio.duplicates.presentation

import android.app.Activity
import androidx.navigation.NavController
import com.example.ads.showInter
import yin_kio.duplicates.domain.models.Destination

class Navigation(
    private val childNavController: NavController,
    private val activity: Activity,
    private val onCloseInter: () -> Unit
) {

    private var currentDestination = Destination.List



    fun navigate(destination: Destination){
        if (currentDestination != destination){
            val navigateUpDestinations = arrayOf(
                Destination.List,
                Destination.AskContinue
            )

            if (navigateUpDestinations.contains(destination)) navigateUp()

            val id = destination.adapt()
            if (id == INTER_ID) {
                activity.showInter(
                    onClosed = onCloseInter
                )
            } else {
                childNavController.navigate(id)
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
            Destination.AskContinue -> R.id.action_duplicatesFragment_to_askContinueDialog
            Destination.Advices -> TODO()
            Destination.AdvicesWithDialog -> TODO()
        }
    }

    private fun navigateUp(){
        childNavController.navigateUp()
    }

    companion object{
        private const val INTER_ID = -1
    }

}