package yin_kio.acceleration.presentation

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator

class NavigatorImpl(
    private val coroutineScope: CoroutineScope
) : AccelerationNavigator {

    var navController: NavController? = null

    override fun close() = onMain {
        navController!!.navigateUp()
    }

    override fun showAccelerateProgress() {
        navController?.navigate(R.id.toAccelerationDialog)
    }


    override fun showPermission() = onMain {
        navController?.navigate(R.id.toAccelerationPermission)
    }

    override fun showInter() {
        TODO("Not yet implemented")
    }

    override fun showSelectableAcceleration() {
        navController?.navigate(R.id.toSelectableAcceleration)
    }

    override fun complete() {
        TODO("Not yet implemented")
    }

    private fun onMain(action: () -> Unit){
        coroutineScope.launch {
            action()
        }
    }

}