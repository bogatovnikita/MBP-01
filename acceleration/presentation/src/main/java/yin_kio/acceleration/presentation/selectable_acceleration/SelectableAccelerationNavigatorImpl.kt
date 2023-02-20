package yin_kio.acceleration.presentation.selectable_acceleration

import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.inter.Inter

class SelectableAccelerationNavigatorImpl(
    private val coroutineScope: CoroutineScope,
    private val inter: Inter,
    private val completeDestination: Int,
    private val completeArgs: Bundle
) : SelectableAccelerationNavigator {

    var navController: NavController? = null


    override fun close() = onMain {
        navController?.navigateUp()
    }

    override fun showStopProgress() = onMain {
        navController?.navigate(R.id.toStopProgress)
    }

    override fun showInter() = onMain {
        inter.show()
    }

    override fun complete() = onMain {
        navController?.popBackStack(R.id.accelerationFragment, true)
        navController?.navigate(completeDestination, completeArgs)
    }

    private fun onMain(action: () -> Unit){
        coroutineScope.launch {
            action()
        }
    }
}