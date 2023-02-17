package yin_kio.acceleration.presentation

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator

class AccelerationNavigatorImpl(
    private val coroutineScope: CoroutineScope,
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

    override fun showSelectableAcceleration() {
        navController?.navigate(R.id.toSelectableAcceleration)
    }

    override fun complete() = onMain {
        TODO("There is must be navigation to advices")
    }

    private fun onMain(action: () -> Unit){
        coroutineScope.launch {
            action()
        }
    }

}