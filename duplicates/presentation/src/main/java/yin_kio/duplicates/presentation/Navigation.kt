package yin_kio.duplicates.presentation

import android.app.Activity
import android.util.Log
import androidx.navigation.NavController
import com.example.ads.showInter
import yin_kio.duplicates.domain.models.Destination

class Navigation(
    private val navController: NavController,
    private val activity: Activity,
    private val onCloseInter: () -> Unit
) {

    private var currentDestination = Destination.List



    fun navigate(destination: Destination){
        Log.d("!!!", "$destination")
        if (currentDestination != destination){
            if (destination == Destination.List) navigateUp()
            val id = destination.adapt()
            if (id == INTER_ID) {
                activity.showInter(
                    onClosed = onCloseInter
                )
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