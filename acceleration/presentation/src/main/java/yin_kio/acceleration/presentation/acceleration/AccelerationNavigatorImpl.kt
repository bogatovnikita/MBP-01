package yin_kio.acceleration.presentation.acceleration

import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.presentation.inter.Inter
import yin_kio.acceleration.presentation.R

class AccelerationNavigatorImpl(
    private val coroutineScope: CoroutineScope,
    private val completeDestination: Int,
    private val completeArgs: Bundle,
    var inter: Inter? = null // Выделение интерфейса для интера помогает упростить тестирование
) : AccelerationNavigator {

    var navController: NavController? = null

    override fun close() = onMain {
        navController!!.navigateUp()
    }

    override fun showAccelerateProgress() = onMain {
        navController?.navigate(R.id.toAccelerationDialog)
    }


    override fun showPermission() = onMain {
        navController?.navigate(R.id.toAccelerationPermission)
    }

    override fun showInter() = onMain {
        inter?.show()
    }

    override fun showSelectableAcceleration() = onMain {
        navController?.navigate(
            R.id.toSelectableAcceleration,
            Bundle().apply { putInt("completeId", completeDestination) }
        )
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